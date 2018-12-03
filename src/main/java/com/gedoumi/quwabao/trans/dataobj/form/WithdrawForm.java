package com.gedoumi.quwabao.trans.dataobj.form;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * 提现表单
 *
 * @author Minced
 */
@Data
public class WithdrawForm {

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 提现金额
     */
    @DecimalMin("0.00001")  // 大于等于0.00001
    @Digits(integer = 10, fraction = 5)  // 整数10位，小数5位
    @NotBlank
    private String amount;

}
