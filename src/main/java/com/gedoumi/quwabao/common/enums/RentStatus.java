package com.gedoumi.quwabao.common.enums;


public enum RentStatus {

    Expired(0,"到期"),
    Active(1,"激活");

    private RentStatus(int value, String name){
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
