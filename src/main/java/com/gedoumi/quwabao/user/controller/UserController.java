package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.asset.dataobj.model.UserAsset;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.common.validate.MobilePhone;
import com.gedoumi.quwabao.user.dataobj.form.ResetPasswordForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.UserInfoVO;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
    @GetMapping("/getUser")
    public ResponseObject getUser() {
        // 获取作用域中用户
        User user = ContextUtil.getUserFromRequest();
        // 获取用户资产
        UserAsset userAsset = userService.getUserAsset(user.getId());
        // 封装返回信息
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setMobilePhone(user.getMobilePhone());
        userInfoVO.setFrozenAsset(String.valueOf(userAsset.getFrozenAsset()));
        userInfoVO.setRemainAsset(String.valueOf(userAsset.getRemainAsset()));
        return new ResponseObject<>(userInfoVO);
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
     * 重置密码（忘记密码）
     *
     * @param resetPasswordForm 重置密码表单
     * @return ResponseObject
     */
    @PutMapping("/resetPswd")
    public ResponseObject resetPswd(@RequestBody @Valid ResetPasswordForm resetPasswordForm) {
        return new ResponseObject<>(userService.resetPswd(resetPasswordForm));
    }

//    /**
//     * 修改密码
//     *
//     * @param pswdVO 修改密码表单
//     * @return ResponseObject
//     */
//    @PutMapping("/updatePswd")
//    public ResponseObject updatePswd(@RequestBody @Valid UpdatePasswordForm pswdVO) {
//
//        ResponseObject responseObject = new ResponseObject();
//        if (StringUtils.isEmpty(pswdVO.getOrgPswd()) || StringUtils.isEmpty(pswdVO.getPswd())) {
//            responseObject.setInfo(CodeEnum.ResetPswdError);
//            return responseObject;
//        }
//        User user = (User) ContextUtil.getSession().getAttribute(Constants.API_USER_KEY);
//        String salt = MD5EncryptUtil.md5Encrypy(user.getMobilePhone());
//        String orgPswd = MD5EncryptUtil.md5Encrypy(pswdVO.getOrgPswd(), salt);
//
//        if (!StringUtils.equals(user.getPassword(), orgPswd)) {
//            responseObject.setInfo(CodeEnum.OrgPswdError);
//            return responseObject;
//        }
//        String pswd = MD5EncryptUtil.md5Encrypy(pswdVO.getPswd(), salt);
//        user.setPassword(pswd);
//        user.setUpdateTime(new Date());
//        userService.update(user);
//
//        responseObject.setSuccess();
//        return responseObject;
//    }
//
//    /**
//     * 修改用户名
//     *
//     * @param resetForm
//     * @return ResponseObject
//     */
//    @PutMapping("/updateUsername")
//    public ResponseObject updateUsername(@RequestBody UpdateUsernameForm resetForm) {
//        User user = (User) ContextUtil.getSession().getAttribute(Constants.API_USER_KEY);
//
//        if (StringUtils.isEmpty(resetForm.getUserName())) {
//            responseObject.setInfo(CodeEnum.NoNameError);
//            return responseObject;
//        }
//
//        User orgUser = userService.getByUsername(resetForm.getUserName());
//        if (orgUser != null) {
//            throw new BusinessException(CodeEnum.NameError);
//        }
//
//        user.setUsername(resetForm.getUserName());
//        user.setUpdateTime(new Date());
//        userService.update(user);
//
//        responseObject.setSuccess();
//        ContextUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
//        return new ResponseObject<>();
//    }

}
