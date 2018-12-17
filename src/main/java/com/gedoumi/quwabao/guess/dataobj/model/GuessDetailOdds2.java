package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 玩法二赔率
 *
 * @author Minced
 */
@Alias("GuessDetailOdds2")
@Data
public class GuessDetailOdds2 {

    private Long id;

    private Long guessDetailId;

    private BigDecimal o12;

    private BigDecimal o13;

    private BigDecimal o14;

    private BigDecimal o15;

    private BigDecimal o16;

    private BigDecimal o23;

    private BigDecimal o24;

    private BigDecimal o25;

    private BigDecimal o26;

    private BigDecimal o34;

    private BigDecimal o35;

    private BigDecimal o36;

    private BigDecimal o45;

    private BigDecimal o46;

    private BigDecimal o56;

}