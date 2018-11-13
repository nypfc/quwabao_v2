package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.MobilePhone;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 短信验证码
     */
    @NotBlank
    private String smsCode;

    /**
     * 邀请码
     */
    @NotBlank
    @Length(min = 8, max = 8, message = "邀请码必须为8位")
    private String inviteCode;

    /**
     * 用户名
     */
    private String username;

}
