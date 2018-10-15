package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 下注状态
 * @author Minced
 */
@Getter
public enum GuessBetStatusEnum {

    NOT_BEGIN(0, "未开始"),
    GUESS_RIGHT(1, "猜中"),
    NOT_GUESS_RIGHT(2, "未猜中")
    ;

    private Integer code;

    private String description;

    GuessBetStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

}
