package com.gedoumi.quwabao.guess.dataobj.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 竞猜详情VO
 *
 * @author Minced
 */
@Data
public class GuessDetailVO {

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
     * 名次
     */
    private String guessResult;

}
