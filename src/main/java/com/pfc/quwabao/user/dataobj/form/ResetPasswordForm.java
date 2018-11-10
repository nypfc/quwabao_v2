package com.pfc.quwabao.user.dataobj.form;

import com.pfc.quwabao.common.validate.MobilePhone;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 重置密码表单
 *
 * @author Minced
 */
@Data
public class ResetPasswordForm {

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

}
