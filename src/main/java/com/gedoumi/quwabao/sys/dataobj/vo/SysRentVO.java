package com.gedoumi.quwabao.sys.dataobj.vo;

import lombok.Data;

/**
 * 矿机信息
 *
 * @author Minced
 */
@Data
public class SysRentVO {

    /**
     * 矿机类型
     */
    private Integer rentType;

    /**
     * 矿机名称
     */
    private String rentName;

    /**
     * 矿机价格
     */
    private String rentMoney;

    /**
     * 总收益
     */
    private String totalRemain;

}
