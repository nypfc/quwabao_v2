package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 玩法二投注金额
 * @author Minced
 */
@Entity
@Table(name = "guess_detail_money2")
@DynamicInsert
@DynamicUpdate
@Data
public class GuessDetailMoney2 {

    /**
     * 玩法二投注金额ID
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
     * 投注金额12
     */
    @Column(name = "m12")
    private BigDecimal m12;

    /**
     * 投注金额13
     */
    @Column(name = "m13")
    private BigDecimal m13;

    /**
     * 投注金额14
     */
    @Column(name = "m14")
    private BigDecimal m14;

    /**
     * 投注金额15
     */
    @Column(name = "m15")
    private BigDecimal m15;

    /**
     * 投注金额16
     */
    @Column(name = "m16")
    private BigDecimal m16;

    /**
     * 投注金额23
     */
    @Column(name = "m23")
    private BigDecimal m23;

    /**
     * 投注金额24
     */
    @Column(name = "m24")
    private BigDecimal m24;

    /**
     * 投注金额25
     */
    @Column(name = "m25")
    private BigDecimal m25;

    /**
     * 投注金额26
     */
    @Column(name = "m26")
    private BigDecimal m26;

    /**
     * 投注金额34
     */
    @Column(name = "m34")
    private BigDecimal m34;

    /**
     * 投注金额35
     */
    @Column(name = "m35")
    private BigDecimal m35;

    /**
     * 投注金额36
     */
    @Column(name = "m36")
    private BigDecimal m36;

    /**
     * 投注金额45
     */
    @Column(name = "m45")
    private BigDecimal m45;

    /**
     * 投注金额46
     */
    @Column(name = "m46")
    private BigDecimal m46;

    /**
     * 投注金额56
     */
    @Column(name = "m56")
    private BigDecimal m56;

    /**
     * 玩法二投注总金额
     */
    @Column(name = "total")
    private BigDecimal total;

}
