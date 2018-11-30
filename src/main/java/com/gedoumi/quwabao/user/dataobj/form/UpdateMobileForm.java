package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.MobilePhone;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新手机号表单
 *
 * @author Minced
 */
@Data
public class UpdateMobileForm {

    /**
     * 手机号
     */
    @NotBlank
    @MobilePhone
    private String newMobile;

    /**
     * 原密码
     */
    @NotBlank
    private String orgPassword;

    /**
     * 短信验证码
     */
    @NotBlank
    private String smsCode;

}
