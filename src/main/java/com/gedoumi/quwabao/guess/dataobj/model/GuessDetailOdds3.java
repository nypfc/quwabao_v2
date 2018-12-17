package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 玩法三赔率
 *
 * @author Minced
 */
@Alias("GuessDetailOdds3")
@Data
public class GuessDetailOdds3 {

    private Long id;

    private Long guessDetailId;

    private BigDecimal o1;

    private BigDecimal o2;

}