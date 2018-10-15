package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞猜用用户资产
 * @author Minced
 */
@Data
@Table(name = "user_asset")
@DynamicInsert
@DynamicUpdate
@Entity
public class GuessUserAsset {

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 剩余资产
     */
    @Column(name = "remain_asset")
    private BigDecimal remainAsset;

    /**
     * 总资产（剩余资产 + 冻结资产）
     */
    @Column(name = "total_asset")
    private BigDecimal totalAsset;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 冻结资产
     */
    @Column(name = "init_frozen_asset")
    private BigDecimal initFrozenAsset;

}
