package com.gedoumi.quwabao.user.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户资产
 *
 * @author Minced
 */
@Alias("UserAsset")
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
     * 上交易所前的天使币（已废弃）
     */
    private BigDecimal initFrozenAsset;

    /**
     * 上交易所前的总资产（已废弃）
     */
    private BigDecimal initBaseAsset;

}
