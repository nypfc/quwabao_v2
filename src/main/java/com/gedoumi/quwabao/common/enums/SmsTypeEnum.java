package com.gedoumi.quwabao.common.enums;

import lombok.Getter;

@Getter
public enum SmsTypeEnum {

    REGISTER(0, "register", "注册"),
    LOGIN(1, "login", "登录"),
    RESET_PASSWORD(2, "resetPwd", "重置密码"),
    UPDATE_PAY_PASSWORD(3, "updatePayPassword", "修改支付密码"),
    UPDATE_MOBILE(4, "updateMobile", "修改手机号"),
    ;

    private int value;

    private String name;

    private String description;

    SmsTypeEnum(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

}
