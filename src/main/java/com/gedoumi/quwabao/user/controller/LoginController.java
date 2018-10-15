package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.base.LoginToken;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CipherUtils;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.common.utils.SessionUtil;
import com.gedoumi.quwabao.common.utils.SmsUtil;
import com.gedoumi.quwabao.common.validate.MobilePhone;
import com.gedoumi.quwabao.sys.entity.SysSms;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.user.dataobj.form.RegForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

/**
 * 登录Controller
 *
 * @author Minced
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v2/login")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private SysSmsService smsService;

    /**
     * 登录
     *
     * @param regForm
     * @return
     */
    @PostMapping("/submit")
    public ResponseObject submit(@RequestBody @Valid RegForm regForm) {

        String salt = MD5EncryptUtil.md5Encrypy(regForm.getMobile());
        String pswd = MD5EncryptUtil.md5Encrypy(regForm.getPassword(), salt);

        User user = userService.checkMobilePhone(regForm.getMobile());
        if (user == null) throw new BusinessException(CodeEnum.UserLoginError);
        if (user.getUserStatus() == UserStatus.Disable.getValue()) throw new BusinessException(CodeEnum.UserLocked);
        if (user.getErrorCount() > 3) throw new BusinessException(CodeEnum.LoginTimesError);

        if (!StringUtils.equals(pswd, user.getPassword())) {
            user.setDeviceId(SessionUtil.getDeviceFromHead());
            userService.updateLoginError(user);
            throw new BusinessException(CodeEnum.UserLoginError);
        }

        SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName(user.getUsername());
        loginToken.setMobilePhone(user.getMobilePhone());
        loginToken.setToken(UUID.randomUUID().toString());
        user.setLastLoginIp(SessionUtil.getClientIp());
        user.setToken(loginToken.getToken());
        user.setDeviceId(SessionUtil.getDeviceFromHead());
        userService.updateLoginInfo(user);

        return new ResponseObject<>(loginToken);
    }

    @PutMapping("/resetPswd")
    public ResponseObject resetPswd(@RequestBody RegForm regForm) {
        ResponseObject responseObject = new ResponseObject();
        Object obj = SessionUtil.getSession().getAttribute(Constants.V_CODE_KEY);
        if (obj == null) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }
        String validateCode = obj.toString();
        if (!StringUtils.equals(validateCode, regForm.getValidateCode().toUpperCase())) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }
        String mobile = regForm.getMobile();
        User user = userService.getByMobilePhone(mobile);

        if (user == null) {
            responseObject.setInfo(CodeEnum.MobileError);
            return responseObject;
        }
        SysSms query = new SysSms();
        query.setMobilePhone(regForm.getMobile());
        query.setCode(regForm.getSmsCode());
        query.setSmsStatus(SmsStatus.Enable.getValue());
        query.setSmsType(SmsType.ResetPswd.getValue());
        SysSms sms = smsService.getUserFul(query);
        if (sms == null) {
            responseObject.setInfo(CodeEnum.ValidateCodeExpire);
            return responseObject;
        }

        String salt = MD5EncryptUtil.md5Encrypy(mobile);
        user.setPassword(MD5EncryptUtil.md5Encrypy(regForm.getPassword(), salt));
        Date now = new Date();
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        user.setLastLoginIp(SessionUtil.getClientIp());
        user.setToken(UUID.randomUUID().toString());
        userService.updateLoginInfo(user);
        responseObject.setSuccess();
        SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName(user.getUsername());
        loginToken.setMobilePhone(user.getMobilePhone());
        loginToken.setToken(user.getToken());
        responseObject.setData(loginToken);
        return responseObject;
    }

    @GetMapping("/logout")
    public ResponseObject logout() {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setSuccess();
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        SessionUtil.getSession().removeAttribute(Constants.API_USER_KEY);
        if (user != null)
            userService.updateLogout(user);
        return responseObject;
    }

    @GetMapping("/getSessionId")
    public ResponseObject getSessionId() {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setSuccess();
        responseObject.setData(SessionUtil.getSession().getId());
        return responseObject;
    }

}
