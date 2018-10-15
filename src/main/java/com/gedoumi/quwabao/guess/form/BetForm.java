package com.gedoumi.quwabao.guess.form;

import lombok.Data;

/**
 * 下注接参类
 * @author Minced
 */
@Data
public class BetForm {

    /**
     * 下注量
     */
    private Double bet;

    /**
     * 下注玩法
     */
    private Integer guessMode;

    /**
     * 下注的号码
     */
    private String guessNumber;

}
