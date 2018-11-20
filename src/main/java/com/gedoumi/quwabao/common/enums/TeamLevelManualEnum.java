package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 强制调整团队等级枚举
 *
 * @author Minced
 */
@Getter
public enum TeamLevelManualEnum {

    AUTO(0, "自动"),
    MANUAL(1, "手动");

    private Integer value;

    private String description;

    TeamLevelManualEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}
