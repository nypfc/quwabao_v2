package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum SmsType {

    Register(0, "register", "注册"),
    Login(1, "login", "登录"),
    ResetPswd(2, "resetPwd", "重置密码"),
    ;

    private int value;

    private String name;

    private String description;

    SmsType(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

}
