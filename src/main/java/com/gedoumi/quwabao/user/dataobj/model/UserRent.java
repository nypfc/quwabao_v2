package com.gedoumi.quwabao.user.dataobj.model;

import com.gedoumi.quwabao.miner.dataobj.model.Rent;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户租用矿机信息
 *
 * @author Minced
 */
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
    private Long rentType;

    /**
     * 矿机
     */
    private Rent rent;

    /**
     * 矿机状态
     * 0:已经结束
     * 1:正在运行
     */
    private Integer rentStatus;

}
