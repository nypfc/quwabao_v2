package com.gedoumi.quwabao.common.enums;


public enum UserType {

    Level_0(0,"普通用户"),
    Level_Team(1,"团队长"),
    ;

    private UserType(int value, String name){
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
