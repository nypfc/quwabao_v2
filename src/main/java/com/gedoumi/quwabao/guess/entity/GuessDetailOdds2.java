package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 玩法二赔率
 * @author Minced
 */
@Entity
@Table(name = "guess_detail_odds2")
@DynamicInsert
@DynamicUpdate
@Data
public class GuessDetailOdds2 {

    /**
     * 玩法二赔率ID
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
     * 赔率12
     */
    @Column(name = "o12")
    private BigDecimal o12;

    /**
     * 赔率13
     */
    @Column(name = "o13")
    private BigDecimal o13;

    /**
     * 赔率14
     */
    @Column(name = "o14")
    private BigDecimal o14;

    /**
     * 赔率15
     */
    @Column(name = "o15")
    private BigDecimal o15;

    /**
     * 赔率16
     */
    @Column(name = "o16")
    private BigDecimal o16;

    /**
     * 赔率23
     */
    @Column(name = "o23")
    private BigDecimal o23;

    /**
     * 赔率24
     */
    @Column(name = "o24")
    private BigDecimal o24;

    /**
     * 赔率25
     */
    @Column(name = "o25")
    private BigDecimal o25;

    /**
     * 赔率26
     */
    @Column(name = "o26")
    private BigDecimal o26;

    /**
     * 赔率34
     */
    @Column(name = "o34")
    private BigDecimal o34;

    /**
     * 赔率35
     */
    @Column(name = "o35")
    private BigDecimal o35;

    /**
     * 赔率36
     */
    @Column(name = "o36")
    private BigDecimal o36;

    /**
     * 赔率45
     */
    @Column(name = "o45")
    private BigDecimal o45;

    /**
     * 赔率46
     */
    @Column(name = "o46")
    private BigDecimal o46;

    /**
     * 赔率56
     */
    @Column(name = "o56")
    private BigDecimal o56;

}
