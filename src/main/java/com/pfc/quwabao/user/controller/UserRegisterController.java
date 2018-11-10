package com.pfc.quwabao.user.controller;

import com.pfc.quwabao.common.utils.ResponseObject;
import com.pfc.quwabao.common.validate.MobilePhone;
import com.pfc.quwabao.user.dataobj.form.RegisterForm;
import com.pfc.quwabao.user.dataobj.model.User;
import com.pfc.quwabao.user.dataobj.vo.LoginTokenVO;
import com.pfc.quwabao.user.service.UserCheckService;
import com.pfc.quwabao.user.service.UserRegisterService;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户注册Controller
 *
 * @author Minced
 */
@Validated
@RequestMapping("/v2/user")
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
    @GetMapping("/check/mobile/{mobile}")
    public ResponseObject checkMobilePhone(@MobilePhone @PathVariable String mobile) {
        return new ResponseObject<>(userCheckService.checkMobilePhone(mobile));
    }

    /**
     * 验证邀请码
     *
     * @param inviteCode 邀请码
     * @return ResponseObject
     */
    @GetMapping("/check/inviteCode/{inviteCode}")
    public ResponseObject checkInviteCode(@Length(min = 8, max = 8, message = "Invite code length must be 8 characters") @PathVariable String inviteCode) {
        return new ResponseObject<>(userCheckService.checkInviteCode(inviteCode));
    }

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return ResponseObject
     */
    @GetMapping("/check/username/{username}")
    public ResponseObject checkUserName(@PathVariable String username) {
        return new ResponseObject<>(userCheckService.checkUsername(username));
    }

    /**
     * 注册
     *
     * @param registerForm 注册表单
     * @return ResponseObject
     */
    @PostMapping("/register")
    public ResponseObject register(@RequestBody @Valid RegisterForm registerForm) {
        User user = userRegisterService.register(registerForm);
        // 封装返回数据
        LoginTokenVO loginTokenVO = new LoginTokenVO();
        loginTokenVO.setToken(user.getToken());
        return new ResponseObject<>();
    }

}
