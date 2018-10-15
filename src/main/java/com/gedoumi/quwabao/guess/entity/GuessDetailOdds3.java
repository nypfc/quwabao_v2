package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 玩法三赔率
 * @author Minced
 */
@Entity
@Table(name = "guess_detail_odds3")
@DynamicInsert
@DynamicUpdate
@Data
public class GuessDetailOdds3 {

    /**
     * 玩法三赔率ID
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

}
