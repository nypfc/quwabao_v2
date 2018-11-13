package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author Minced
 */
@Getter
public enum UserTypeEnum {

    NORMAL(0, "普通用户"),
    LEADER(1, "团队长"),
    ;

    private int value;

    private String name;

    UserTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
