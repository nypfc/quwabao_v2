package com.gedoumi.quwabao.guess.dataobj.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "MM-dd", timezone = "GMT+8")
    private Date date;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    private Integer betStatus;

}
