package com.pfc.quwabao.common.enums;


public enum SysLogStatus {

    Init(0,"初始化"),
    Success(1,"成功"),
    Fail(2,"失败");

    private SysLogStatus(int value, String name){
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
