package com.gedoumi.quwabao.user.dataobj.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新支付密码表单
 */
@Data
public class UpdatePayPasswordForm {

    /**
     * 原支付密码
     * 用户可能没有支付密码，不需要进行非空验证
     */
    private String originalPassword;

    /**
     * 新支付密码
     */
    @NotBlank
    private String password;

}
