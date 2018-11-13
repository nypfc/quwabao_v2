package com.gedoumi.quwabao.sys.dataobj.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 矿机
 *
 * @author Minecd
 */
@TableName("sys_rent")
@Data
public class SysRent {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 矿机价格
     */
    private BigDecimal money;

    /**
     * 矿机名称
     */
    private String name;

    /**
     * 每日收益
     */
    private BigDecimal profitDay;

    /**
     * 预计收益
     */
    private BigDecimal profitMoney;

    /**
     * 总收益
     */
    private BigDecimal profitMoneyExt;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 矿机类型
     */
    private Integer rentCode;

    /**
     * 矿机状态
     * 0:已关闭租用
     * 1:已开启租用
     */
    private Integer rentStatus;

}
