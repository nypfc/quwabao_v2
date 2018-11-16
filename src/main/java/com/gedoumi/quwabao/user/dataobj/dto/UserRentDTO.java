package com.gedoumi.quwabao.user.dataobj.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 用户矿机DTO
 */
@Alias("UserRentDTO")
@Data
public class UserRentDTO {

    /**
     * 矿机ID
     */
    private Long id;

    /**
     * 矿机租金
     */
    private BigDecimal rentAsset;

    /**
     * 每日收益量
     */
    private BigDecimal profitDay;

    /**
     * 已经挖出的收益
     */
    private BigDecimal alreadyDig;

    /**
     * 总收益
     */
    private BigDecimal totalAsset;

    /**
     * 用户ID
     */
    private Long userId;

}
