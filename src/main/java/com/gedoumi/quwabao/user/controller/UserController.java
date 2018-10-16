package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.api.face.FaceApi;
import com.gedoumi.quwabao.api.face.IDApi;
import com.gedoumi.quwabao.api.face.IDApiResponse;
import com.gedoumi.quwabao.api.face.vo.FaceVO;
import com.gedoumi.quwabao.common.base.LoginToken;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.common.utils.SmsUtil;
import com.gedoumi.quwabao.common.validate.MobilePhone;
import com.gedoumi.quwabao.sys.dataobj.model.SysLog;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import com.gedoumi.quwabao.sys.service.SysLogService;
import com.gedoumi.quwabao.user.dataobj.form.RegisterForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.BindRegVO;
import com.gedoumi.quwabao.user.dataobj.vo.PswdVO;
import com.gedoumi.quwabao.user.dataobj.vo.ValidateUserVO;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

/**
 * 用户Controller
 *
 * @author Minced
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v2/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private SysLogService logService;

    @GetMapping("/getUser")
    public ResponseObject getUser(@NotBlank @MobilePhone String mobile) {
        ResponseObject responseObject = new ResponseObject();
        User user = userService.getByMobilePhone(mobile);
        if (user != null) {
            responseObject.setData(user);
        }
        responseObject.setSuccess();
        return responseObject;
    }

    @GetMapping("/getRpSmsCode")
    public ResponseObject getRpSmsCode(@NotBlank @MobilePhone String mobile, @NotBlank String vcode) {
        ResponseObject responseObject = new ResponseObject();

        Object obj = ContextUtil.getSession().getAttribute(Constants.V_CODE_KEY);
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

    @PutMapping("/resetPswd")
    public ResponseObject resetPswd(@RequestBody RegisterForm registerForm) {
        ResponseObject responseObject = new ResponseObject();
        Object obj = ContextUtil.getSession().getAttribute(Constants.V_CODE_KEY);
        if (obj == null) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }
        String validateCode = obj.toString();
        if (!StringUtils.equals(validateCode, registerForm.getValidateCode().toUpperCase())) {
            responseObject.setInfo(CodeEnum.ValidateCodeError);
            return responseObject;
        }
        String mobile = registerForm.getMobile();
        User user = userService.getByMobilePhone(mobile);

        if (user == null) {
            responseObject.setInfo(CodeEnum.MobileError);
            return responseObject;
        }
        SysSms query = new SysSms();
        query.setMobilePhone(registerForm.getMobile());
        query.setCode(registerForm.getSmsCode());
        query.setSmsStatus(SmsStatus.Enable.getValue());
        query.setSmsType(SmsType.ResetPswd.getValue());
        SysSms sms = smsService.getUserFul(query);
        if (sms == null) {
            responseObject.setInfo(CodeEnum.ValidateCodeExpire);
            return responseObject;
        }

        String salt = MD5EncryptUtil.md5Encrypy(mobile);
        user.setPassword(MD5EncryptUtil.md5Encrypy(registerForm.getPassword(), salt));
        Date now = new Date();
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        user.setLastLoginIp(ContextUtil.getClientIp());
        user.setToken(UUID.randomUUID().toString());
        userService.updateLoginInfo(user);
        responseObject.setSuccess();
        ContextUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName(user.getUsername());
        loginToken.setMobilePhone(user.getMobilePhone());
        loginToken.setToken(user.getToken());
        responseObject.setData(loginToken);
        return responseObject;
    }

    @RequestMapping("/updatePswd")
    public ResponseObject updatePswd(@RequestBody PswdVO pswdVO) {

        ResponseObject responseObject = new ResponseObject();
        if (StringUtils.isEmpty(pswdVO.getOrgPswd()) || StringUtils.isEmpty(pswdVO.getPswd())) {
            responseObject.setInfo(CodeEnum.ResetPswdError);
            return responseObject;
        }
        User user = (User) ContextUtil.getSession().getAttribute(Constants.API_USER_KEY);
        String salt = MD5EncryptUtil.md5Encrypy(user.getMobilePhone());
        String orgPswd = MD5EncryptUtil.md5Encrypy(pswdVO.getOrgPswd(), salt);

        if (!StringUtils.equals(user.getPassword(), orgPswd)) {
            responseObject.setInfo(CodeEnum.OrgPswdError);
            return responseObject;
        }
        String pswd = MD5EncryptUtil.md5Encrypy(pswdVO.getPswd(), salt);
        user.setPassword(pswd);
        user.setUpdateTime(new Date());
        userService.update(user);

        responseObject.setSuccess();
        return responseObject;
    }

    @RequestMapping("/updateUsername")
    public ResponseObject updateUsername(@RequestBody BindRegVO bindRegVO) {
        ResponseObject responseObject = new ResponseObject();
        User user = (User) ContextUtil.getSession().getAttribute(Constants.API_USER_KEY);

        if (StringUtils.isEmpty(bindRegVO.getUserName())) {
            responseObject.setInfo(CodeEnum.NoNameError);
            return responseObject;
        }

        User orgUser = userService.getByUsername(bindRegVO.getUserName());
        if (orgUser != null) {
            responseObject.setInfo(CodeEnum.NameError);
            return responseObject;
        }

        user.setUsername(bindRegVO.getUserName());
        user.setUpdateTime(new Date());
        userService.update(user);

        responseObject.setSuccess();
        ContextUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
        return responseObject;
    }

    @RequestMapping(value = "/valUser")
    public ResponseObject validateUser(@RequestBody ValidateUserVO validateUserVO) {
        log.info("in valUser ");
        ResponseObject responseObject = new ResponseObject();
        if (
                StringUtils.isEmpty(validateUserVO.getIdCard()) ||
                        StringUtils.isEmpty(validateUserVO.getRealName())) {
            responseObject.setInfo(CodeEnum.ValidateIdParamError);
            return responseObject;
        }

        Date now = new Date();
        User user = (User) ContextUtil.getSession().getAttribute(Constants.API_USER_KEY);
        if (user.getValidateStatus() != null && user.getValidateStatus() == UserValidateStatus.Pass.getValue()) {
            responseObject.setInfo(CodeEnum.ValidateAlready);
            return responseObject;
        }

        User orgUser = userService.getByIdCard(validateUserVO.getIdCard());
        if (orgUser != null && !orgUser.getId().equals(user.getId())) {
            responseObject.setInfo(CodeEnum.ValidateIdCardAlready);
            return responseObject;
        }

        validateUserVO.setUserId(user.getId());
        FaceVO faceVO = new FaceVO();
        faceVO.setMall_id(FaceApi.mall_id);
        faceVO.setIdcard(validateUserVO.getIdCard());
        faceVO.setImage(validateUserVO.getBase64Img());
        faceVO.setRealname(validateUserVO.getRealName());
        faceVO.setTm(String.valueOf(System.currentTimeMillis()));
        faceVO.setSign(faceVO.generateSign());
        IDApiResponse idApiResponse = new IDApiResponse();
        SysLog sysLog = new SysLog();
        sysLog.setRequestBody(StringUtils.EMPTY);
        sysLog.setLogType(LogType.FaceValidate.getValue());
        sysLog.setMobile(user.getMobilePhone());
        sysLog.setRequestUrl(FaceApi.getUrl() + FaceApi.path);
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogStatus(SysLogStatus.Fail.getValue());
        sysLog.setClientIp(ContextUtil.getClientIp());
        logService.add(sysLog);
        try {
            idApiResponse = IDApi.testID(faceVO);
        } catch (Exception e) {
            log.error("call face api error", e);
            responseObject.setData(false);
        }
        userService.validateUser(validateUserVO, idApiResponse);
        if (idApiResponse.isSucess()) {
            sysLog.setLogStatus(SysLogStatus.Success.getValue());
            logService.update(sysLog);
            responseObject.setData(true);
        } else {
            responseObject.setData(false);
        }

        User valUser = userService.getByMobilePhone(user.getMobilePhone());
        ContextUtil.getSession().setAttribute(Constants.API_USER_KEY, valUser);
        responseObject.setSuccess();
        return responseObject;
    }

}
