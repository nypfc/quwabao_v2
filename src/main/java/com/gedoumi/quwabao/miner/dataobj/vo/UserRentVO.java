package com.gedoumi.quwabao.miner.dataobj.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 矿机
 *
 * @author Minced
 */
@Data
public class UserRentVO {

    /**
     * 矿机名称
     */
    private String rentName;

    /**
     * 上次挖矿收益
     */
    private String lastDig;

    /**
     * 剩余收益
     */
    private String remainProfit;

}
