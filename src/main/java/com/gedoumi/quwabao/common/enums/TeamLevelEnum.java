package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 俱乐部等级
 *
 * @author Minced
 */
@Getter
public enum TeamLevelEnum {

    LEVEL_0(0, "普通用户"),
    LEVEL_1(1, "明星俱乐部"),
    LEVEL_2(2, "冠军俱乐部"),
    LEVEL_3(3, "富豪俱乐部"),
    LEVEL_4(4, "皇家俱乐部"),
    ;

    private Integer value;

    private String description;

    TeamLevelEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
