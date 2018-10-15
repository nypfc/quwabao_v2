package com.gedoumi.quwabao.common.enums;


public enum UserValidateStatus {

    Init(0,"未认证"),
    Pass(1,"通过实名认证"),
    UnPass(2,"未通过实名认证");

    private UserValidateStatus(int value, String name){
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
