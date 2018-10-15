package com.gedoumi.quwabao.common.enums;


public enum VersionType {

    WithReharge(0,"包括本金"),
    WithoutRecharge(1,"不包括本金");

    private VersionType(int value, String name){
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
