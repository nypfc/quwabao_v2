package com.gedoumi.quwabao.user.dataobj.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户团队信息表
 */
@TableName("user_team_ext")
@Data
public class UserTeamExt {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户等级
     */
    private Integer teamLevel;

    /**
     * 团队每日产币量
     */
    private BigDecimal teamTotalStaticProfit;

    /**
     * 团队业绩
     */
    private BigDecimal teamTotalRent;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();

}
