package com.gedoumi.quwabao.user.dataobj.vo;

import lombok.Data;

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
