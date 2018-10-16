package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum SmsType {

    Register(0, "注册"),
    Login(1, "登录"),
    ResetPswd(2, "重置密码"),
    ;

    private int value;

    private String name;

    SmsType(int value, String name) {
        this.value = value;
        this.name = name;
    }

}
