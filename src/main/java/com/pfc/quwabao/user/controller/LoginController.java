package com.pfc.quwabao.user.controller;

import com.pfc.quwabao.common.utils.ContextUtil;
import com.pfc.quwabao.common.utils.ResponseObject;
import com.pfc.quwabao.user.dataobj.form.LoginForm;
import com.pfc.quwabao.user.dataobj.model.User;
import com.pfc.quwabao.user.dataobj.vo.LoginTokenVO;
import com.pfc.quwabao.user.service.LoginService;
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
