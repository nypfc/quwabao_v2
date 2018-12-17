package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 密码类型枚举
 *
 * @author Minced
 */
@Getter
public enum PasswordTypeEnum {

    LOGIN(1, "登录密码"),
    PAYMENT(2, "支付密码")
    ;

    private Integer value;

    private String description;

    PasswordTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
