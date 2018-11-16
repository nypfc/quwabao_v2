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
     * 当日静态收益
     */
    private BigDecimal currentDayStaticProfit;

}
