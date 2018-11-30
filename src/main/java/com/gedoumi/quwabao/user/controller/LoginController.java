package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.form.LoginForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.LoginTokenVO;
import com.gedoumi.quwabao.user.service.LoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.gedoumi.quwabao.common.constants.ApiConstants.APP_LOGIN;

/**
 * 登录Controller
 *
 * @author Minced
 */
@Validated
@RestController
@RequestMapping(APP_LOGIN)
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     *
     * @param loginForm 登录表单
     * @return ResponseObject
     */
    @PostMapping
    public ResponseObject submit(@RequestBody @Valid LoginForm loginForm) {
        User user = loginService.login(loginForm);
        // 封装返回信息
        LoginTokenVO loginTokenVO = new LoginTokenVO();
        loginTokenVO.setToken(user.getToken());
        return new ResponseObject<>(loginTokenVO);
    }

    /**
     * 退出
     *
     * @return ResponseObject
     */
    @DeleteMapping
    public ResponseObject logout() {
        loginService.logout(ContextUtil.getTokenFromHead());
        return new ResponseObject();
    }

}
