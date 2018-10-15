package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 玩法一赔率
 * @author Minced
 */
@Entity
@Table(name = "guess_detail_odds1")
@DynamicInsert
@DynamicUpdate
@Data
public class GuessDetailOdds1 {

    /**
     * 玩法一赔率ID
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
     * 赔率1
     */
    @Column(name = "o1")
    private BigDecimal o1;

    /**
     * 赔率2
     */
    @Column(name = "o2")
    private BigDecimal o2;

    /**
     * 赔率3
     */
    @Column(name = "o3")
    private BigDecimal o3;

    /**
     * 赔率4
     */
    @Column(name = "o4")
    private BigDecimal o4;

    /**
     * 赔率5
     */
    @Column(name = "o5")
    private BigDecimal o5;

    /**
     * 赔率6
     */
    @Column(name = "o6")
    private BigDecimal o6;

}
