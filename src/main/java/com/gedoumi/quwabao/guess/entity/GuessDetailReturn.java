package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 玩法二实际返奖率
 * @author Minced
 */
@Entity
@Table(name = "guess_detail_return")
@DynamicInsert
@DynamicUpdate
@Data
public class GuessDetailReturn {

    /**
     * 玩法二返奖率ID
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
     * 返奖率12
     */
    @Column(name = "r12")
    private BigDecimal r12;

    /**
     * 返奖率13
     */
    @Column(name = "r13")
    private BigDecimal r13;

    /**
     * 返奖率14
     */
    @Column(name = "r14")
    private BigDecimal r14;

    /**
     * 返奖率15
     */
    @Column(name = "r15")
    private BigDecimal r15;

    /**
     * 返奖率16
     */
    @Column(name = "r16")
    private BigDecimal r16;

    /**
     * 返奖率21
     */
    @Column(name = "r21")
    private BigDecimal r21;

    /**
     * 返奖率23
     */
    @Column(name = "r23")
    private BigDecimal r23;

    /**
     * 返奖率24
     */
    @Column(name = "r24")
    private BigDecimal r24;

    /**
     * 返奖率25
     */
    @Column(name = "r25")
    private BigDecimal r25;

    /**
     * 返奖率26
     */
    @Column(name = "r26")
    private BigDecimal r26;

    /**
     * 返奖率31
     */
    @Column(name = "r31")
    private BigDecimal r31;

    /**
     * 返奖率32
     */
    @Column(name = "r32")
    private BigDecimal r32;

    /**
     * 返奖率34
     */
    @Column(name = "r34")
    private BigDecimal r34;

    /**
     * 返奖率35
     */
    @Column(name = "r35")
    private BigDecimal r35;

    /**
     * 返奖率36
     */
    @Column(name = "r36")
    private BigDecimal r36;

    /**
     * 返奖率41
     */
    @Column(name = "r41")
    private BigDecimal r41;

    /**
     * 返奖率42
     */
    @Column(name = "r42")
    private BigDecimal r42;

    /**
     * 返奖率43
     */
    @Column(name = "r43")
    private BigDecimal r43;

    /**
     * 返奖率45
     */
    @Column(name = "r45")
    private BigDecimal r45;

    /**
     * 返奖率46
     */
    @Column(name = "r46")
    private BigDecimal r46;

    /**
     * 返奖率51
     */
    @Column(name = "r51")
    private BigDecimal r51;

    /**
     * 返奖率52
     */
    @Column(name = "r52")
    private BigDecimal r52;

    /**
     * 返奖率53
     */
    @Column(name = "r53")
    private BigDecimal r53;

    /**
     * 返奖率54
     */
    @Column(name = "r54")
    private BigDecimal r54;

    /**
     * 返奖率56
     */
    @Column(name = "r56")
    private BigDecimal r56;

    /**
     * 返奖率61
     */
    @Column(name = "r61")
    private BigDecimal r61;

    /**
     * 返奖率62
     */
    @Column(name = "r62")
    private BigDecimal r62;

    /**
     * 返奖率63
     */
    @Column(name = "r63")
    private BigDecimal r63;

    /**
     * 返奖率64
     */
    @Column(name = "r64")
    private BigDecimal r64;

    /**
     * 返奖率65
     */
    @Column(name = "r65")
    private BigDecimal r65;

}
