package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author Minced
 */
@Getter
public enum UserStatusEnum {

    DISABLE(0, "禁用"),
    ENABLE(1, "可用"),
    ;

    private int value;

    private String name;

    UserStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
