package com.pfc.quwabao.common.enums;

import lombok.Getter;

/**
 * 竞猜详情状态枚举
 * @author Minced
 */
@Getter
public enum GuessDetailStatusEnum {

    NOT_STARTED(0, "尚未开始"),
    BETTING(1, "下注期"),
    GAMING(2, "游戏期"),
    BOUNS(3, "算奖期"),
    FINISHED(4, "已结束")
    ;

    private Integer code;

    private String message;

    GuessDetailStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
