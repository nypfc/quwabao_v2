package com.gedoumi.quwabao.common.enums;


public enum TransDetailType {

    RemainTrans(0,"余额转账"),
    FrozenTrans(1,"冻结资产转账"),
    ;

    private TransDetailType(int value, String name){
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


    public static TransDetailType fromValue(int value){
        TransDetailType[] valus = TransDetailType.values();
        for (TransDetailType transType : valus) {
            if(transType.getValue() == value){
                return transType;
            }
        }
        return null;
    }
}
