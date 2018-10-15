package com.gedoumi.quwabao.common.enums;


import java.math.BigDecimal;

public enum UnFrozenType {

    Level_1(1, new BigDecimal("2"), new BigDecimal("0.3"), "2倍业绩解冻30%"),
    Level_2(2, new BigDecimal("4"), new BigDecimal("0.6"), "4倍业绩解冻60%"),
    Level_3(3, new BigDecimal("5"), new BigDecimal("1"), "5倍业绩解冻100%"),
    ;


    UnFrozenType(int value, BigDecimal rate, BigDecimal unFrozenRate, String name) {
        this.value = value;
        this.rate = rate;
        this.unFrozenRate = unFrozenRate;
        this.name = name;
    }

    private int value;
    private BigDecimal rate;//业绩比例
    private BigDecimal unFrozenRate;//解冻比例
    private String name;


    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public BigDecimal getUnFrozenRate() {
        return unFrozenRate;
    }

    public void setUnFrozenRate(BigDecimal unFrozenRate) {
        this.unFrozenRate = unFrozenRate;
    }

    public static UnFrozenType fromValue(int value){
        for (UnFrozenType type :UnFrozenType.values()) {
            if(type.getValue() == value){
                return type;
            }
        }
        return null;
    }
}
