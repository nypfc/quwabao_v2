package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.asset.dataobj.model.UserAsset;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.dto.UserInfoDTO;
import com.gedoumi.quwabao.user.dataobj.form.ResetPasswordForm;
import com.gedoumi.quwabao.user.dataobj.form.UpdatePasswordForm;
import com.gedoumi.quwabao.user.dataobj.form.UpdateUsernameForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.LoginTokenVO;
import com.gedoumi.quwabao.user.dataobj.vo.UserInfoVO;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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

    /**
     * 获取用户数据
     *
     * @return ResponseObject
     */
    @GetMapping
    public ResponseObject getUserInfo() {
        // 获取用户信息
        UserInfoDTO userInfo = userService.getUserInfo();
        User user = userInfo.getUser();
        UserAsset userAsset = userInfo.getUserAsset();
        // 封装返回信息
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setMobilePhone(user.getMobilePhone());
        userInfoVO.setFrozenAsset(String.valueOf(userAsset.getFrozenAsset()));
        userInfoVO.setRemainAsset(String.valueOf(userAsset.getRemainAsset()));
        return new ResponseObject<>(userInfoVO);
    }

    /**
     * 重置密码（忘记密码）
     *
     * @param resetPasswordForm 重置密码表单
     * @return ResponseObject
     */
    @PutMapping("/resetPswd")
    public ResponseObject resetPassword(@RequestBody @Valid ResetPasswordForm resetPasswordForm) {
        User user = userService.resetPassword(resetPasswordForm);
        // 封装返回数据
        LoginTokenVO loginTokenVO = new LoginTokenVO();
        loginTokenVO.setUserName(user.getUsername());
        loginTokenVO.setMobilePhone(user.getMobilePhone());
        loginTokenVO.setToken(user.getToken());
        return new ResponseObject<>(loginTokenVO);
    }

    /**
     * 修改密码
     *
     * @param updatePasswordForm 修改密码表单
     * @return ResponseObject
     */
    @PutMapping("/updatePswd")
    public ResponseObject updatePassword(@RequestBody @Valid UpdatePasswordForm updatePasswordForm) {
        userService.updatePassword(updatePasswordForm);
        return new ResponseObject();
    }

    /**
     * 修改用户名
     *
     * @param updateUsernameForm 修改用户名表单
     * @return ResponseObject
     */
    @PutMapping("/updateUsername")
    public ResponseObject updateUsername(@RequestBody @Valid UpdateUsernameForm updateUsernameForm) {
        userService.updateUsername(updateUsernameForm);
        return new ResponseObject<>();
    }

}
