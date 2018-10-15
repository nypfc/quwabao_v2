package com.gedoumi.quwabao.guess.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户下注记录
 * @author Minced
 */
@Data
public class GuessBetVO {

    /**
     * 日期
     */
    private String date;

    /**
     * 期号
     */
    private String issueNumber;

    /**
     * 下注的玩法
     */
    private String guessMode;

    /**
     * 下注的号码
     */
    private String guessNumber;

    /**
     * 开始时间
     */
    private Date betTime;

    /**
     * 下注奖金
     */
    private String betMoney;

    /**
     * 下注选项对应赔率
     */
    private String betOdds;

    /**
     * 中奖金额
     */
    private String resultBouns;

    /**
     * 下注状态
     */
    private String betStatus;

}
