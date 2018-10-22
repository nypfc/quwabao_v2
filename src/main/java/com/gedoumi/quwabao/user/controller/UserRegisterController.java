package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.validate.MobilePhone;
import com.gedoumi.quwabao.user.dataobj.form.RegisterForm;
import com.gedoumi.quwabao.user.service.UserCheckService;
import com.gedoumi.quwabao.user.service.UserRegisterService;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 用户注册Controller
 *
 * @author Minced
 */
@Validated
@RequestMapping("/v2/login")
@RestController
public class UserRegisterController {

    @Resource
    private UserCheckService userCheckService;
    @Resource
    private UserRegisterService userRegisterService;

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
     * @param mobile 手机号
     * @return ResponseObject
     */
    @GetMapping("/validateCode")
    public ResponseObject generateValidateCode(@NotBlank @MobilePhone String mobile) {
        return new ResponseObject<>(userRegisterService.generateValidateCode(mobile));
    }

    /**
     * 发送注册用短信验证码
     *
     * @param mobile 手机号
     * @param vcode  验证码
     * @return ResponseObject
     */
    @GetMapping("/getRegSmsCode")
    public ResponseObject getRegSmsCode(@NotBlank @MobilePhone String mobile, @NotBlank String vcode) {
        userRegisterService.getRegSmsCode(mobile, vcode);
        return new ResponseObject();
    }

    /**
     * 注册
     *
     * @param registerForm 注册表单
     * @return ResponseObject
     */
    @PostMapping("/reg")
    public ResponseObject register(@RequestBody @Valid RegisterForm registerForm) {
        return new ResponseObject<>(userRegisterService.register(registerForm));
    }

}
