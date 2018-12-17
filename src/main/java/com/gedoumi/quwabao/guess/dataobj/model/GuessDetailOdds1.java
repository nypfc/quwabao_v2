package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 玩法一赔率
 *
 * @author Minced
 */
@Alias("GuessDetailOdds1")
@Data
public class GuessDetailOdds1 {

    private Long id;

    private Long guessDetailId;

    private BigDecimal o1;

    private BigDecimal o2;

    private BigDecimal o3;

    private BigDecimal o4;

    private BigDecimal o5;

    private BigDecimal o6;

}