package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 玩法三投注金额
 * @author Minced
 */
@Entity
@Table(name = "guess_detail_money3")
@DynamicInsert
@DynamicUpdate
@Data
public class GuessDetailMoney3 {

    /**
     * 玩法三投注金额ID
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
     * 玩法三投注总金额
     */
    @Column(name = "total")
    private BigDecimal total;

}
