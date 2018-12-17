package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户下注
 *
 * @author Minced
 */
@Alias("GuessBet")
@Data
public class GuessBet {

    private Long id;

    private Long userId;

    private Long guessDetailId;

    private Date betTime;

    private Integer guessMode;

    private Integer guessNumber;

    private BigDecimal betMoney;

    private BigDecimal betOdds;

    private BigDecimal resultBouns;

    private Integer betStatus;

}