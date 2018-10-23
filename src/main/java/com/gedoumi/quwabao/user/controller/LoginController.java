package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.form.LoginForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.LoginTokenVO;
import com.gedoumi.quwabao.user.service.LoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 登录Controller
 *
 * @author Minced
 */
@Validated
@RestController
@RequestMapping("/v2/login")
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     *
     * @param loginForm 登录表单
     * @return ResponseObject
     */
    @PostMapping("/submit")
    public ResponseObject submit(@RequestBody @Valid LoginForm loginForm) {
        User user = loginService.login(loginForm);
        LoginTokenVO loginTokenVO = new LoginTokenVO();
        loginTokenVO.setUserName(user.getUsername());
        loginTokenVO.setMobilePhone(user.getMobilePhone());
        loginTokenVO.setToken(user.getToken());
        return new ResponseObject<>(loginTokenVO);
    }

    /**
     * 退出
     *
     * @return ResponseObject
     */
    @GetMapping("/logout")
    public ResponseObject logout() {
        loginService.logout();
        return new ResponseObject();
    }

}
