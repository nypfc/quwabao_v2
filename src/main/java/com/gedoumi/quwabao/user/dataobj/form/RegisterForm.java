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
    @Length(min = 8, max = 8, message = "邀请码必须为8位")
    private String regInviteCode;

    /**
     * 用户名
     */
    private String userName;

}
