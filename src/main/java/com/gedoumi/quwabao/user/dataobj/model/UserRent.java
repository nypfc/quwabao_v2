package com.gedoumi.quwabao.user.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户租用矿机信息
 *
 * @author Minced
 */
@Alias("UserRent")
@Data
public class UserRent {

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * （冗余字段）
     */
    private Integer days;

    /**
     * 结束时间
     */
    private Date expireDate;

    /**
     * 矿机价格
     */
    private BigDecimal rentAsset;

    /**
     * 上次挖矿收益
     */
    private BigDecimal lastDig;

    /**
     * 已经获得的收益
     */
    private BigDecimal alreadyDig;

    /**
     * 总收益
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
     * 矿机类型
     */
    private Integer rentType;

    /**
     * （冗余字段）
     */
    private Integer digNumber;

    /**
     * 第一次购买矿机标识
     */
    private Integer firstRentType;

    /**
     * 矿机状态
     * 0:已经结束
     * 1:正在运行
     */
    private Integer rentStatus;

}
