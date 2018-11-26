package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 网关日志类型枚举
 *
 * @author Minced
 */
@Getter
public enum SysLogTypeEnum {

    RECHARGE(0, "充值到PFC"),
    WITHDRAW(1, "提现到网关"),
    QUERY_ETH_ADDRESS(2, "查询eth地址"),
    BIND_ETH_ADDRESS(3, "绑定eth地址");

    private int value;

    private String name;

    SysLogTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
