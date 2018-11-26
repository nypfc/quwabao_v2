package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

/**
 * 网关日志状态枚举
 *
 * @author Minced
 */
@Getter
public enum SysLogStatusEnum {

    INIT(0, "初始化"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败");

    private int value;

    private String name;

    SysLogStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
