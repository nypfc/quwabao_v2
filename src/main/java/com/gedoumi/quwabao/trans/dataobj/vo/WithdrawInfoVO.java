package com.gedoumi.quwabao.trans.dataobj.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现信息VO
 *
 * @author Minced
 */
@Data
public class WithdrawInfoVO {

    /**
     * 单次提现最小限额
     */
    private BigDecimal singleMin;

    /**
     * 单次提现最大金额
     */
    private BigDecimal singleMax;

    /**
     * 每日提现最大限额
     */
    private BigDecimal dayLimit;

    /**
     * 当日剩余提现金额
     */
    private BigDecimal remainLimit;

}
