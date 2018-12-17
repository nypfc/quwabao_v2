package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 竞猜详情状态枚举
 * @author Minced
 */
@Getter
public enum GuessDetailStatusEnum {

    INIT(0, "初始化状态"),
    BETTING(1, "下注期"),
    GAMING(2, "游戏期"),
    SHOW_RESULT(3, "结果展示期"),
    FINISHED(4, "已结束")
    ;

    private Integer value;

    private String message;

    GuessDetailStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

}
