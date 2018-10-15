package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 玩法一投注金额
 * @author Minced
 */
@Entity
@Table(name = "guess_detail_money1")
@DynamicInsert
@DynamicUpdate
@Data
public class GuessDetailMoney1 {

    /**
     * 玩法一投注金额ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 竞猜详情ID
     */
    @Column(name = "guess_detail_id")
    private Long guessDetailId;

    /**
     * 投注金额1
     */
    @Column(name = "m1")
    private BigDecimal m1;

    /**
     * 投注金额2
     */
    @Column(name = "m2")
    private BigDecimal m2;

    /**
     * 投注金额3
     */
    @Column(name = "m3")
    private BigDecimal m3;

    /**
     * 投注金额4
     */
    @Column(name = "m4")
    private BigDecimal m4;

    /**
     * 投注金额5
     */
    @Column(name = "m5")
    private BigDecimal m5;

    /**
     * 投注金额6
     */
    @Column(name = "m6")
    private BigDecimal m6;

    /**
     * 玩法一投注总金额
     */
    @Column(name = "total")
    private BigDecimal total;

}
