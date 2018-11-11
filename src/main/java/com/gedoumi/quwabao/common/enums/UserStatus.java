package com.gedoumi.quwabao.common.enums;


public enum UserStatus {

    Disable(0,"禁用"),
    Enable(1,"可用");

    private UserStatus(int value, String name){
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
