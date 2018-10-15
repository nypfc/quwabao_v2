package com.gedoumi.quwabao.common.enums;


public enum FirstRentType {

    Init(0,""),
    First(1,"第一次"),
    ;

    private FirstRentType(int value, String name){
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
