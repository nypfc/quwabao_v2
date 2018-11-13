package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 用户矿机状态枚举
 *
 * @author Minced
 */
@Getter
public enum UserRentStatusEnum {

    EXPIRED(0, "已结束的矿机"),
    ACTIVE(1, "正在激活中的矿机");

    private int value;

    private String name;

    UserRentStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
