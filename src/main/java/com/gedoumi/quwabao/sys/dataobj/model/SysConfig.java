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
     * APP版本
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

}