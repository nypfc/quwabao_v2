package com.gedoumi.quwabao.user.dataobj.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户资产DTO
 *
 * @author Minced
 */
@Data
public class UserAssetDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收益量
     */
    private BigDecimal profit;

    /**
     * 更新时间
     */
    private Date updateTime;

}
