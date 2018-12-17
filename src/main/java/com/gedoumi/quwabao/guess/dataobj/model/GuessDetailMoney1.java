package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 玩法一下注金额
 *
 * @author Minced
 */
@Alias("GuessDetailMoney1")
@Data
public class GuessDetailMoney1 {

    private Long id;

    private Long guessDetailId;

    private BigDecimal m1;

    private BigDecimal m2;

    private BigDecimal m3;

    private BigDecimal m4;

    private BigDecimal m5;

    private BigDecimal m6;

    private BigDecimal total;

}