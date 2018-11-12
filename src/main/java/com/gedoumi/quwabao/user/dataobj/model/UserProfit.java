package com.gedoumi.quwabao.user.dataobj.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户收益表
 *
 * @author Minced
 */
@TableName("user_profit")
@Data
public class UserProfit {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
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
