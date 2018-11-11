package com.gedoumi.quwabao.common.enums;


public enum LogType {

    Recharge(0,"充值到PFC"),
    WithDraw(1,"提现到网关"),
    QueryEthAddress(2,"查询eth地址"),
    BindEthAddress(3,"绑定eth地址"),
    FaceValidate(4,"实名认证"),
    ;

    private LogType(int value, String name){
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
