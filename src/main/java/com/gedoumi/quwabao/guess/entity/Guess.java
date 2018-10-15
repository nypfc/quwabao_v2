package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞猜实体类
 * @author Minced
 */
@Data
@Table(name = "guess")
@DynamicInsert
@DynamicUpdate
@Entity
public class Guess {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 当前竞猜期开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 当前竞猜期结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 当前竞猜期赔付率
     */
    @Column(name = "odds")
    private BigDecimal odds;

    /**
     * 当前竞猜期下注时间
     */
    @Column(name = "bet_time")
    private Integer betTime;

    /**
     * 游戏持续时间
     */
    @Column(name = "game_time")
    private Integer gameTime;

    /**
     * 计算奖金的时间
     */
    @Column(name = "bouns_time")
    private Integer bounsTime;

    /**
     * 返奖率
     */
    @Column(name = "guess_return")
    private BigDecimal guessReturn;

    /**
     * 竞猜状态
     */
    @Column(name = "guess_status")
    private Integer guessStatus;

}
