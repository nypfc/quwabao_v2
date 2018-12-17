package com.gedoumi.quwabao.guess.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞猜期
 *
 * @author Minced
 */
@Alias("Guess")
@Data
public class Guess {

    private Long id;

    private Date startTime;

    private Date endTime;

    private BigDecimal odds;

    private Integer betTime;

    private Integer gameTime;

    private Integer bounsTime;

    private BigDecimal guessReturn;

    private Integer guessStatus;

}