package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户竞猜下注
 * @author Minced
 */
@Data
@Table(name = "guess_bet")
@DynamicInsert
@DynamicUpdate
@Entity
public class GuessBet {

    /**
     * ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 竞猜详情ID
     */
    @Column(name = "guess_detail_id")
    private Long guessDetailId;

    /**
     * 下注时间
     */
    @Column(name = "bet_time")
    private Date betTime;

    /**
     * 下注玩法
     */
    @Column(name = "guess_mode")
    private Integer guessMode;

    /**
     * 下注的号码
     */
    @Column(name = "guess_number")
    private String guessNumber;

    /**
     * 下注金额
     */
    @Column(name = "bet_money")
    private BigDecimal betMoney;

    /**
     * 下注时选项的赔率
     */
    @Column(name = "bet_odds")
    private BigDecimal betOdds;

    /**
     * 最后用户实际获得的奖金
     */
    @Column(name = "result_bouns")
    private BigDecimal resultBouns;

    /**
     * 下注状态
     */
    @Column(name = "bet_status")
    private Integer betStatus;

}
