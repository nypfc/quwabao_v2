package com.gedoumi.quwabao.guess.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 竞猜用用户资产详情
 * @author Minced
 */
@Data
@Table(name = "user_asset_detail")
@DynamicInsert
@DynamicUpdate
@Entity
public class GuessUserAssetDetail {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 交易金额（下注扣款用）
     */
    @Column(name = "money")
    private BigDecimal money;

    /**
     * 收益（竞猜获胜奖金用）
     */
    @Column(name = "profit")
    private BigDecimal profit;

    /**
     * 收益（挖矿用算法，竞猜无用）
     */
    @Column(name = "profit_ext")
    private BigDecimal profitExt;

    /**
     * 交易类型
     */
    @Column(name = "trans_type")
    private Integer transType;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 版本号（固定）
     */
    @Column(name = "version_type")
    private Integer versionType;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

}
