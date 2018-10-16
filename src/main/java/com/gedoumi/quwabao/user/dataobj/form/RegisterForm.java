package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.MobilePhone;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册表单
 *
 * @author Minced
 */
@Data
public class RegisterForm {

    /**
     * 手机号
     */
    @MobilePhone
    @NotBlank
    private String mobile;

    /**
     * 验证码
     */
    @NotBlank
    private String validateCode;

    /**
     * 短信验证码
     */
    @NotBlank
    private String smsCode;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 邀请码
     */
    @NotBlank
    private String regInviteCode;

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

}
