package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.api.face.FaceApi;
import com.gedoumi.quwabao.api.face.IDApi;
import com.gedoumi.quwabao.api.face.IDApiResponse;
import com.gedoumi.quwabao.api.face.vo.FaceVO;
import com.gedoumi.quwabao.common.base.LoginToken;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
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
import javax.validation.Valid;
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

    /**
     * 获取用户数据
     *
     * @param mobile 手机号
     * @return ResponseObject
     */
    @GetMapping("/getUser")
    public ResponseObject getUser(@NotBlank @MobilePhone String mobile) {
        User user = userService.getByMobilePhone(mobile);
        // TODO 使用VO代替
        return new ResponseObject<>(user);
    }

    /**
     * 获取重置密码用的短信验证码
     *
     * @param mobile 手机号
     * @param vcode  验证码
     * @return ResponseObject
     */
    @GetMapping("/getRpSmsCode")
    public ResponseObject getRpSmsCode(@NotBlank @MobilePhone String mobile, @NotBlank String vcode) {
        return new ResponseObject<>(userService.getRpSmsCode(mobile, vcode));
    }

    /**
     * 重置密码
     *
     * @param registerForm
     * @return ResponseObject
     */
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

        user.setPassword(MD5EncryptUtil.md5Encrypy(registerForm.getPassword(), MD5EncryptUtil.md5Encrypy(mobile)));
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

    @PutMapping("/updatePswd")
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

    @PutMapping("/updateUsername")
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

    /**
     * 实名认证
     *
     * @param validateUserVO 实名认证表单
     * @return ResponseObject
     */
    @PostMapping("/valUser")  // TODO 参数验证
    public ResponseObject validateUser(@RequestBody @Valid ValidateUserVO validateUserVO) {
        log.info("in valUser ");

        Date now = new Date();
        User user = ContextUtil.getUserFromRequest();
        if (user.getValidateStatus() != null && user.getValidateStatus() == UserValidateStatus.Pass.getValue()) {
            log.error("用户:{}已经经过实名认证", user.getMobilePhone());
            throw new BusinessException(CodeEnum.ValidateAlready);
        }

        User orgUser = userService.getByIdCard(validateUserVO.getIdCard());
        if (orgUser != null && !orgUser.getId().equals(user.getId())) {
            log.error("身份证号:{}已被使用", validateUserVO.getIdCard());
            throw new BusinessException(CodeEnum.ValidateIdCardAlready);
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

        boolean result;
        try {
            idApiResponse = IDApi.testID(faceVO);
        } catch (Exception e) {
            log.error("call face api error", e);
            result = false;
        }
        userService.validateUser(validateUserVO, idApiResponse);
        if (idApiResponse.isSucess()) {
            sysLog.setLogStatus(SysLogStatus.Success.getValue());
            logService.update(sysLog);
            result = true;
        } else {
            result = false;
        }

        User valUser = userService.getByMobilePhone(user.getMobilePhone());
        ContextUtil.getSession().setAttribute(Constants.API_USER_KEY, valUser);
        return new ResponseObject<>(result);
    }

}
