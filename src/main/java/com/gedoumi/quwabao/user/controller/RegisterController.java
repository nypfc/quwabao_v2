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
import com.gedoumi.quwabao.user.dataobj.form.RegForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.service.UserCheckService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

/**
 * 用户注册Controller
 *
 * @author Minced
 */
@Slf4j
@Validated
@RequestMapping("/v2/login")
@RestController
public class RegisterController {

    @Resource
    private UserCheckService userCheckService;

    /**
     * 验证手机号
     *
     * @param mobile 手机号
     * @return ResponseObject
     */
    @GetMapping("/check")
    public ResponseObject checkMobilePhone(@NotBlank @MobilePhone String mobile) {
        return new ResponseObject<>(userCheckService.checkMobilePhone(mobile));
    }

    /**
     * 验证邀请码
     *
     * @param regInviteCode 注册邀请码
     * @return ResponseObject
     */
    @GetMapping("/checkInviteCode")
    public ResponseObject checkInviteCode(@NotBlank @Length(min = 4, max = 4) String regInviteCode) {
        return new ResponseObject<>(userCheckService.checkInviteCode(regInviteCode));
    }

    /**
     * 验证用户名
     *
     * @param userName 用户名
     * @return ResponseObject
     */
    @GetMapping("/checkName")
    public ResponseObject checkUserName(@NotBlank String userName) {
        return new ResponseObject<>(userCheckService.checkUsername(userName));
    }

    /**
     * 产生验证码
     *
     * @return ResponseObject
     */
    @GetMapping("/validateCode")
    public ResponseObject generateValidateCode() {
        String validateCode = CipherUtils.generateValidateCode();
        SessionUtil.getSession().setAttribute(Constants.V_CODE_KEY, validateCode);
        return new ResponseObject<>(validateCode);
    }

    /**
     * @param mobile
     * @param vcode
     * @return
     */
    @GetMapping("/getRegSmsCode")
    public ResponseObject getRegSmsCode(@NotBlank @MobilePhone String mobile, @NotBlank String vcode) {
        log.info(SessionUtil.getSession().getId());
        ResponseObject responseObject = new ResponseObject();

        Object obj = SessionUtil.getSession().getAttribute(Constants.V_CODE_KEY);
        if (obj == null) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }
        String validateCode = obj.toString();
        if (!StringUtils.equals(validateCode, vcode.toUpperCase())) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }

        User user = userService.getByMobilePhone(mobile);

        if (user != null) {
            responseObject.setInfo(CodeEnum.MobileExist);
            return responseObject;
        }

        int smsCount = smsService.getCurrentDayCount(mobile);
        if (smsCount >= Constants.SMS_DAY_COUNT) {
            responseObject.setInfo(CodeEnum.SmsCountError);
            return responseObject;
        }

        String smsCode = SmsUtil.generateCode();
        try {
            SmsUtil.sendReg(mobile, smsCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
            responseObject.setInfo(CodeEnum.SysError);
            return responseObject;
        }
        SysSms sms = new SysSms();
        sms.setCode(smsCode);
        sms.setSmsStatus(SmsStatus.Enable.getValue());
        sms.setSmsType(SmsType.Register.getValue());
        sms.setMobilePhone(mobile);
        Date now = new Date();
        sms.setCreateTime(now);
        sms.setUpdateTime(now);
        smsService.add(sms);
        responseObject.setSuccess();
        return responseObject;
    }

    @GetMapping("/getRpSmsCode")
    public ResponseObject getRpSmsCode(String mobile, String vcode) {
        ResponseObject responseObject = new ResponseObject();
        if (!SmsUtil.isPhone(mobile)) {
            responseObject.setInfo(CodeEnum.MobileError);
            return responseObject;
        }

        Object obj = SessionUtil.getSession().getAttribute(Constants.V_CODE_KEY);
        if (obj == null) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }
        String validateCode = obj.toString();
        if (!StringUtils.equals(validateCode, vcode)) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }

        User user = userService.getByMobilePhone(mobile);

        if (user == null) {
            responseObject.setInfo(CodeEnum.MobileError);
            return responseObject;
        }

        int smsCount = smsService.getCurrentDayCount(mobile);
        if (smsCount >= Constants.SMS_DAY_COUNT) {
            responseObject.setInfo(CodeEnum.SmsCountError);
            return responseObject;
        }

        String smsCode = SmsUtil.generateCode();
        try {
            SmsUtil.sendReg(mobile, smsCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
            responseObject.setInfo(CodeEnum.SysError);
            return responseObject;
        }
        SysSms sms = new SysSms();
        sms.setCode(smsCode);
        sms.setSmsStatus(SmsStatus.Enable.getValue());
        sms.setSmsType(SmsType.ResetPswd.getValue());
        sms.setMobilePhone(mobile);
        Date now = new Date();
        sms.setCreateTime(now);
        sms.setUpdateTime(now);
        smsService.add(sms);
        responseObject.setSuccess();
        return responseObject;
    }


    @PostMapping("/reg")
    public ResponseObject register(@RequestBody RegForm regForm) {
        ResponseObject responseObject = new ResponseObject();

        if (StringUtils.isEmpty(regForm.getRegInviteCode())) {
            responseObject.setInfo(CodeEnum.InviteCodeError);
            return responseObject;
        }

        String mobile = regForm.getMobile();
        User user = userService.getByMobilePhone(mobile);

        if (user != null) {
            responseObject.setInfo(CodeEnum.MobileExist);
            return responseObject;
        }
        SysSms query = new SysSms();
        query.setMobilePhone(regForm.getMobile());
        query.setCode(regForm.getSmsCode());
        query.setSmsStatus(SmsStatus.Enable.getValue());
        query.setSmsType(SmsType.Register.getValue());
        SysSms sms = smsService.getUserFul(query);
        if (sms == null) {
            responseObject.setInfo(CodeEnum.ValidateCodeExpire);
            return responseObject;
        }

        if (StringUtils.isNotEmpty(regForm.getUserName())) {
            User orgUser = userService.getByUsername(regForm.getUserName());
            if (orgUser != null) {
                responseObject.setInfo(CodeEnum.NameError);
                return responseObject;
            }
        }

        User parentUser = userService.getByInviteCode(regForm.getRegInviteCode().toLowerCase());
        if (parentUser == null) {
            responseObject.setInfo(CodeEnum.InviteCodeError);
            return responseObject;
        }


        User sysUser = new User();
        sysUser.setMobilePhone(mobile);
        String salt = MD5EncryptUtil.md5Encrypy(mobile);
        sysUser.setPassword(MD5EncryptUtil.md5Encrypy(regForm.getPassword(), salt));
        sysUser.setUserStatus(UserStatus.Enable.getValue());
        Date now = new Date();
        sysUser.setUpdateTime(now);
        sysUser.setRegisterTime(now);
        sysUser.setLastLoginTime(now);
        sysUser.setLastLoginIp(SessionUtil.getClientIp());
        sysUser.setToken(UUID.randomUUID().toString());
        sysUser.setUserType(UserType.Level_0.getValue());
        sysUser.setErrorCount(Short.valueOf("0"));
        sysUser.setDeviceId(SessionUtil.getDeviceFromHead());
        sysUser.setValidateStatus(UserValidateStatus.Init.getValue());

        sysUser.setRegInviteCode(regForm.getRegInviteCode());
        if (StringUtils.isNotEmpty(regForm.getUserName())) {
            sysUser.setUsername(regForm.getUserName());
        }

        try {
            userService.addUser(sysUser);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            responseObject.setInfo(e.getCodeEnum());
            return responseObject;
        }

        responseObject.setSuccess();
        SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, sysUser);
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName(sysUser.getUsername());
        loginToken.setMobilePhone(sysUser.getMobilePhone());
        loginToken.setToken(sysUser.getToken());
        responseObject.setData(loginToken);
        return responseObject;
    }

}
