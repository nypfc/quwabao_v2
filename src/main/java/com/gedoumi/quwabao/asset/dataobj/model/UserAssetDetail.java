package com.gedoumi.quwabao.asset.dataobj.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户资产详情
 *
 * @author Minecd
 */
@Data
public class UserAssetDetail {

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 资产变动金额
     */
    private BigDecimal money;

    /**
     * 带本金的收益（挖矿收益用）
     */
    private BigDecimal profit;

    /**
     * 不带本金的收益（挖矿收益用）
     */
    private BigDecimal profitExt;

    /**
     * 资产变动类型
     */
    private Integer transType;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 版本
     */
    private Integer versionType;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 矿机ID
     */
    private Long rentId;

    /**
     * 挖矿收益日期
     */
    private Date digDate;

    /**
     * 从哪个用户获取的动态收益
     */
    private Long rewardUserId;

    /**
     * 交易所订单ID
     */
    private String apiTransSeq;

    /**
     *
     */
    private BigDecimal fee;

}
