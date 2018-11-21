package com.gedoumi.quwabao.user.dataobj.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 用户团队信息DTO
 *
 * @author Minced
 */
@Alias("UserTeamDTO")
@Data
public class UserTeamDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 总静态收益
     */
    private BigDecimal totalStaticProfit = BigDecimal.ZERO;

    /**
     * 总矿机价格
     */
    private BigDecimal totalRentAsset = BigDecimal.ZERO;

}
