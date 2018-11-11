package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum SmsStatus {

    Disable(0, "禁用"),
    Enable(1, "可用"),
    ;

    private int value;

    private String name;

    SmsStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
