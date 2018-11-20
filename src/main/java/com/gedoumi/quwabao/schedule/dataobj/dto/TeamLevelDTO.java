package com.gedoumi.quwabao.schedule.dataobj.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 团队等级DTO
 *
 * @author Minced
 */
@Data
public class TeamLevelDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户团队等级
     */
    private Integer teamLevel;

    /**
     * 最大用户团杜等级
     */
    private Integer maxTeamLevel = 0;

    /**
     * 累计静态收益
     */
    private BigDecimal totalStaticProfit = BigDecimal.ZERO;

    /**
     * 累计团队业绩（矿机总和）
     */
    private BigDecimal totalRentAsset = BigDecimal.ZERO;

}
