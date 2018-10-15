package com.gedoumi.quwabao.guess.vo;

import lombok.Data;

/**
 * 历史记录VO
 * @author Minced
 */
@Data
public class GuessHistoryVO {

    /**
     * 日期
     */
    private String date;

    /**
     * 期号
     */
    private String issueNumber;

    /**
     * 名次
     */
    private String guessResult;

    /**
     * 冠军队
     */
    private String successTeam;

}
