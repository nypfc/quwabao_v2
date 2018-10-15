package com.gedoumi.quwabao.guess.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 下注DTO
 */
@Data
public class GuessBetDTO {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 竞猜详情ID
     */
    private Long guessDetailId;

    /**
     * 竞猜详情期号
     */
    private String issueNumber;

    /**
     * 下注时间
     */
    private Date betTime;

    /**
     * 下注玩法
     */
    private Integer guessMode;

    /**
     * 下注的号码
     */
    private String guessNumber;

    /**
     * 下注金额
     */
    private BigDecimal betMoney;

    /**
     * 对应选项的赔率
     */
    private BigDecimal betOdds;

    /**
     * 中奖金额
     */
    private BigDecimal resultBouns;

    /**
     * 下注状态
     */
    private Integer betStatus;

}
