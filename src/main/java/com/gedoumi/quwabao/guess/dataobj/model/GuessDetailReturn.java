package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 实际返奖率
 *
 * @author Minced
 */
@Alias("GuessDetailReturn")
@Data
public class GuessDetailReturn {

    private Long id;

    private Long guessDetailId;

    private BigDecimal r12;

    private BigDecimal r13;

    private BigDecimal r14;

    private BigDecimal r15;

    private BigDecimal r16;

    private BigDecimal r21;

    private BigDecimal r23;

    private BigDecimal r24;

    private BigDecimal r25;

    private BigDecimal r26;

    private BigDecimal r31;

    private BigDecimal r32;

    private BigDecimal r34;

    private BigDecimal r35;

    private BigDecimal r36;

    private BigDecimal r42;

    private BigDecimal r41;

    private BigDecimal r43;

    private BigDecimal r45;

    private BigDecimal r46;

    private BigDecimal r51;

    private BigDecimal r52;

    private BigDecimal r53;

    private BigDecimal r54;

    private BigDecimal r56;

    private BigDecimal r61;

    private BigDecimal r62;

    private BigDecimal r63;

    private BigDecimal r64;

    private BigDecimal r65;

}