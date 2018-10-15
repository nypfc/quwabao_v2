package com.gedoumi.quwabao.common.enums;


public enum SmsType {

    Register(0,"注册"),
    Login(1,"登录"),
    ResetPswd(2,"重置密码");

    private SmsType(int value, String name){
        this.value = value;
        this.name = name;
    }

    private int value;

    private String name;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
