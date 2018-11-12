package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 用户收益VO枚举
 *
 * @author Minced
 */
@Getter
public enum UserProfitVOEnum {

    STATIC_PROFIT(1, "静态收益"),
    DYNAMIC_PROFIT(2, "动态收益"),
    CLUB_PROFIT(3, "俱乐部收益"),
    ;

    private Integer value;

    private String description;

    UserProfitVOEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
