package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 充值状态枚举
 *
 * @author Minced
 */
@Getter
public enum RechargeStatusEnum {

    SUCCESS(0, "ok"),
    UNKNOWN_ACCOUNT(2001, "unknown pfc_account"),
    INVALID_ACCESS(9001, "invalid access");

    private Integer value;

    private String message;

    RechargeStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

}
