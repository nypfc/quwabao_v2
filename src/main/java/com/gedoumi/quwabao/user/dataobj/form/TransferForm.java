package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.MobilePhone;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
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
    @DecimalMin("0.00001")  // 大于等于0.00001
    @Digits(integer = 10, fraction = 5)  // 整数10位，小数5位
    @NotBlank
    private String transMoney;

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
