package com.gedoumi.quwabao.guess.dataobj.form;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * 下注表单
 *
 * @author Minced
 */
@Data
public class BetForm {

    /**
     * 下注量
     */
    @NotBlank
    @DecimalMin("0.00001")  // 大于等于0.00001
    @Digits(integer = 10, fraction = 5)  // 整数10位，小数5位
    private String bet;

    /**
     * 下注玩法
     */
    @NotBlank
    private String guessMode;

    /**
     * 下注的号码
     */
    @NotBlank
    private String guessNumber;

}
