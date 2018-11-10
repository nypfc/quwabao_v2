package com.pfc.quwabao.user.dataobj.model;

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
    private Date createTime = new Date();

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
    private Date updateTime = new Date();

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 冗余字段
     */
    private BigDecimal initFrozenAsset = BigDecimal.ZERO;

    /**
     * 冗余字段
     */
    private BigDecimal initBaseAsset = BigDecimal.ZERO;

}
