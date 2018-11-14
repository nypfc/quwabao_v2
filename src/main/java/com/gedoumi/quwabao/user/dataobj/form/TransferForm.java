package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.MobilePhone;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 转账表单
 *
 * @author Minced
 */
@Data
public class TransferForm {

    /**
     * 转账金额
     */
    @Min(0)
    @NotBlank
    private String transMoney;

    /**
     * 转账手机号
     */
    @NotBlank
    @MobilePhone
    private String fromMobile;

    /**
     * 收款手机号
     */
    @NotBlank
    @MobilePhone
    private String toMobile;

    /**
     * 密码
     */
    @NotBlank
    private String password;

}
