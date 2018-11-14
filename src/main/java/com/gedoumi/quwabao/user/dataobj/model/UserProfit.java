package com.gedoumi.quwabao.user.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户收益表
 *
 * @author Minced
 */
@Alias("UserProfit")
@Data
public class UserProfit {

    /**
     * ID
     */
    private Long id;

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
     * 日期
     */
    private Date date;

    /**
     * 创建时间
     */
    private Date createTime;

}
