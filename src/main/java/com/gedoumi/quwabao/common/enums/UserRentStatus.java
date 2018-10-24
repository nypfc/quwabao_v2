package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum UserRentStatus {

    Expired(0, "到期"),
    Active(1, "激活");

    private int value;

    private String name;

    UserRentStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
