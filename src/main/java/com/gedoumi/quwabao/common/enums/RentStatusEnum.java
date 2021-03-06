package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 矿机状态枚举
 *
 * @author Minced
 */
@Getter
public enum RentStatusEnum {

    STOP(0, "停止租用"),
    ACTIVE(1, "正常"),
    ;

    private Integer value;

    private String description;

    RentStatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
