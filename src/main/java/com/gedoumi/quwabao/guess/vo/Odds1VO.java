package com.gedoumi.quwabao.guess.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 玩法一赔率
 * @author Minced
 */
@Data
public class Odds1VO {

    /**
     * 赔率1
     */
    private BigDecimal o1;

    /**
     * 赔率2
     */
    private BigDecimal o2;

    /**
     * 赔率3
     */
    private BigDecimal o3;

    /**
     * 赔率4
     */
    private BigDecimal o4;

    /**
     * 赔率5
     */
    private BigDecimal o5;

    /**
     * 赔率6
     */
    private BigDecimal o6;

}
