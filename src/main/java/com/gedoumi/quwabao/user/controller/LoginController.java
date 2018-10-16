package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.form.LoginForm;
import com.gedoumi.quwabao.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    private LoginService loginService;

    /**
     * 登录
     *
     * @param loginForm 登录表单
     * @return ResponseObject
     */
    @PostMapping("/submit")
    public ResponseObject submit(@RequestBody @Valid LoginForm loginForm) {
        return new ResponseObject<>(loginService.login(loginForm));
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
