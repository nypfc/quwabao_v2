package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 转账状态枚举
 *
 * @author Minced
 */
@Getter
public enum TransStatusEnum {

    Failed(0, "失败"),
    Success(1, "成功");

    private int value;

    private String name;

    TransStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
