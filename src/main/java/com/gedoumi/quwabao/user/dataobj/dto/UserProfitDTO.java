package com.gedoumi.quwabao.user.dataobj.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 用户收益DTO
 *
 * @author Minced
 */
@Alias("UserProfitDTO")
@Data
public class UserProfitDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 静态收益
     */
    private BigDecimal staticProfit;

    /**
     * 动态收益
     */
    private BigDecimal dynamicProfit;

    /**
     * 俱乐部收益
     */
    private BigDecimal clubProfit;

    /**
     * 总收益
     */
    private BigDecimal totalProfit;

    /**
     * 当日已激活的总矿机价格
     */
    private BigDecimal totalRentAsset;

}
