package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞猜详情（小竞猜周期）
 *
 * @author Minced
 */
@Alias("GuessDetail")
@Data
public class GuessDetail {

    private Long id;

    private Long guessId;

    private Integer issueNumber;

    private Date startTime;

    private String guessResult;

    private BigDecimal guessRealReturn;

    private BigDecimal totalBouns;

    private Integer guessDetailStatus;

}