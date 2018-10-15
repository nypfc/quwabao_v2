package com.gedoumi.quwabao.guess.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 玩法三赔率
 * @author Minced
 */
@Data
public class Odds3VO {

    /**
     * 赔率1
     */
    private BigDecimal o1;

    /**
     * 赔率2
     */
    private BigDecimal o2;

}
