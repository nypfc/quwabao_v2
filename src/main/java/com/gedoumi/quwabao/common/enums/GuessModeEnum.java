package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 竞猜玩法枚举
 * @author Minced
 */
@Getter
public enum GuessModeEnum {

    MODE_1(1, "玩法1"),
    MODE_2(2, "玩法2"),
    MODE_3(3, "玩法3"),
    ;

    private Integer mode;

    private String description;

    GuessModeEnum(Integer mode, String description) {
        this.mode = mode;
        this.description = description;
    }

}
