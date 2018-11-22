package com.gedoumi.quwabao.sys.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 系统配置
 *
 * @author Minced
 */
@Alias("SysConfig")
@Data
public class SysConfig {

    /**
     * ID
     */
    private Long id;

    /**
     * 版本号
     */
    private String appVersion;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 静态收益比例
     */
    private BigDecimal staticProfit;

    /**
     * 动态收益比例
     */
    private BigDecimal dynamicProfit;

    /**
     * 转账手续费比例
     */
    private BigDecimal transFeeProportion;

    /**
     * 每次提现最小金额
     */
    private BigDecimal withdrawSingleMin;

    /**
     * 每次提现最大金额
     */
    private BigDecimal withdrawSingleMax;

    /**
     * 每日提现最大金额
     */
    private BigDecimal withdrawDayLimit;

}