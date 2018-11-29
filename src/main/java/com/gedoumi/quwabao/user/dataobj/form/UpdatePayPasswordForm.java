package com.gedoumi.quwabao.user.dataobj.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新支付密码表单
 */
@Data
public class UpdatePayPasswordForm {

    /**
     * 支付密码
     */
    @NotBlank
    private String password;

    /**
     * 短信验证码
     */
    @NotBlank
    private String smsCode;

}
