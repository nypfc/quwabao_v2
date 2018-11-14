package com.gedoumi.quwabao.user.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 团队信息
 *
 * @author Minced
 */
@Alias("UserTeamExt")
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
     * 团队等级
     */
    private Integer teamLevel;

    /**
     * 团队总静态收益
     */
    private BigDecimal teamTotalStaticProfit;

    /**
     * 团队总挖矿量
     */
    private BigDecimal teamTotalRent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
