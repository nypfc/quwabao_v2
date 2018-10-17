package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.common.validate.MobilePhone;
import com.gedoumi.quwabao.sys.service.SysLogService;
import com.gedoumi.quwabao.user.dataobj.form.ResetForm;
import com.gedoumi.quwabao.user.dataobj.form.ResetPswdForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.vo.PswdVO;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;

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
     * @param resetPswdForm 重置密码
     * @return ResponseObject
     */
    @PutMapping("/resetPswd")
    public ResponseObject resetPswd(@RequestBody @Valid ResetPswdForm resetPswdForm) {
        return new ResponseObject<>(userService.resetPswd(resetPswdForm));
    }

    /**
     * 修改密码
     *
     * @param pswdVO 修改密码表单
     * @return ResponseObject
     */
    @PutMapping("/updatePswd")
    public ResponseObject updatePswd(@RequestBody @Valid PswdVO pswdVO) {

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

    /**
     * 修改用户名
     *
     * @param resetForm
     * @return ResponseObject
     */
    @PutMapping("/updateUsername")
    public ResponseObject updateUsername(@RequestBody ResetForm resetForm) {
        User user = (User) ContextUtil.getSession().getAttribute(Constants.API_USER_KEY);

        if (StringUtils.isEmpty(resetForm.getUserName())) {
            responseObject.setInfo(CodeEnum.NoNameError);
            return responseObject;
        }

        User orgUser = userService.getByUsername(resetForm.getUserName());
        if (orgUser != null) {
            throw new BusinessException(CodeEnum.NameError);
        }

        user.setUsername(resetForm.getUserName());
        user.setUpdateTime(new Date());
        userService.update(user);

        responseObject.setSuccess();
        ContextUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
        return new ResponseObject<>();
    }

}
