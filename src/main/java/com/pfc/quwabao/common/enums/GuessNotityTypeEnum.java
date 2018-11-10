package com.pfc.quwabao.common.enums;

import lombok.Getter;

/**
 * WebSocket通知类型枚举
 * @author Minced
 */
@Getter
public enum GuessNotityTypeEnum {

    CONNECTION_SUCCESS(1, "WebSocket连接成功"),
    BET_START(2, "投注开始"),
    GAME_START(3, "比赛开始"),
    GUESS_END(4, "比赛结束")
    ;

    private Integer type;

    private String message;

    GuessNotityTypeEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

}
