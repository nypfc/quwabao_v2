package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 竞猜期状态枚举
 *
 * @author Minced
 */
@Getter
public enum GuessStatusEnum {

    NOT_STARTED(0, "未开始"),
    BEGINNING(1, "正在竞猜期"),
    FINISHED(2, "已结束");

    private Integer value;

    private String message;

    GuessStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

}
