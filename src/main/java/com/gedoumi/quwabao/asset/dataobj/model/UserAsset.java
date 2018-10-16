package com.gedoumi.quwabao.asset.dataobj.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户资产
 *
 * @author Minced
 */
@Data
public class UserAsset {

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 天使钻
     */
    private BigDecimal frozenAsset;

    /**
     * 累计收益
     */
    private BigDecimal profit;

    /**
     * 余额
     */
    private BigDecimal remainAsset;

    /**
     * 总额
     */
    private BigDecimal totalAsset;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 上线交易所后，初始的解冻资产
     */
    private BigDecimal initFrozenAsset = BigDecimal.ZERO;

    /**
     * 上线交易所后，初始的业绩基数,本人及所有下线天使钻之和
     */
    private BigDecimal initBaseAsset = BigDecimal.ZERO;

}
