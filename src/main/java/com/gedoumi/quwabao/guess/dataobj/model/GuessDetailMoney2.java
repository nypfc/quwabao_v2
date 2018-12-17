package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 玩法二下注金额
 *
 * @author Minced
 */
@Alias("GuessDetailMoney2")
@Data
public class GuessDetailMoney2 {

    private Long id;

    private Long guessDetailId;

    private BigDecimal m12;

    private BigDecimal m13;

    private BigDecimal m14;

    private BigDecimal m15;

    private BigDecimal m16;

    private BigDecimal m23;

    private BigDecimal m24;

    private BigDecimal m25;

    private BigDecimal m26;

    private BigDecimal m34;

    private BigDecimal m35;

    private BigDecimal m36;

    private BigDecimal m45;

    private BigDecimal m46;

    private BigDecimal m56;

    private BigDecimal total;

}