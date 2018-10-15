package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞猜详情
 * @author Minced
 */
@Data
@Table(name = "guess_detail")
@DynamicInsert
@DynamicUpdate
@Entity
public class GuessDetail {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 竞猜ID
     */
    @Column(name = "guess_id")
    private Long guessId;

    /**
     * 竞猜期号
     */
    @Column(name = "issue_number")
    private Integer issueNumber;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 比赛结果
     */
    @Column(name = "guess_result")
    private String guessResult;

    /**
     * 实际返奖率
     */
    @Column(name = "guess_real_return")
    private BigDecimal guessRealReturn;

    /**
     * 总奖金
     */
    @Column(name = "total_bouns")
    private BigDecimal totalBouns;

    /**
     * 竞猜详情状态
     */
    @Column(name = "guess_detail_status")
    private Integer guessDetailStatus;

}
