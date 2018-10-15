package com.gedoumi.quwabao.common.enums;


public enum DetailType {

    All(0,"全部"),
    Trans(1,"交易"),
    Profit(2,"收益"),
    ;

    private DetailType(int value, String name){
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


    public static DetailType fromValue(int value){
        DetailType[] valus = DetailType.values();
        for (DetailType transType : valus) {
            if(transType.getValue() == value){
                return transType;
            }
        }
        return null;
    }
}
