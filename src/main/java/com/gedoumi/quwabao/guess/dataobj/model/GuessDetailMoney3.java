package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 玩法三下注金额
 *
 * @author Minced
 */
@Alias("GuessDetailMoney3")
@Data
public class GuessDetailMoney3 {

    private Long id;

    private Long guessDetailId;

    private BigDecimal m1;

    private BigDecimal m2;

    private BigDecimal total;

}