package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 第一次租用矿机标识枚举
 *
 * @author Minced
 */
@Getter
public enum FirstRentTypeEnum {

    NOT_FIRST(0, "非第一次购买"),
    FIRST(1, "第一次购买");

    private Integer value;

    private String description;

    FirstRentTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
