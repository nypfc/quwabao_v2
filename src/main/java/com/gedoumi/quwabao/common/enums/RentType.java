package com.gedoumi.quwabao.common.enums;


import java.math.BigDecimal;

public enum RentType {

    Rent1(0, new BigDecimal("1000"), new BigDecimal("0.02"),new BigDecimal("20"), new BigDecimal("1020"),30, "基础型", 100, 130),
    Rent2(1, new BigDecimal("3000"), new BigDecimal("0.04"), new BigDecimal("120"),new BigDecimal("3120"),30, "标准型",130, 150),
    Rent3(2, new BigDecimal("5000"), new BigDecimal("0.044"),new BigDecimal("220"), new BigDecimal("5220"),30, "专业型",150,180),
    Rent4(3, new BigDecimal("10000"), new BigDecimal("0.05"), new BigDecimal("500"),new BigDecimal("10500"),30, "豪华型",180,210),
    ;

//    Rent1(0, new BigDecimal("3000"), new BigDecimal("0.04"),new BigDecimal("120"), new BigDecimal("3120"),30, "基础型", 100, 130),
//    Rent2(1, new BigDecimal("5000"), new BigDecimal("0.044"), new BigDecimal("220"),new BigDecimal("5220"),30, "标准型",130, 150),
//    Rent3(2, new BigDecimal("10000"), new BigDecimal("0.05"),new BigDecimal("500"), new BigDecimal("10500"),30, "专业型",150,180),
//    Rent4(3, new BigDecimal("30000"), new BigDecimal("0.068"), new BigDecimal("2040"),new BigDecimal("32040"),30, "豪华型",180,210),
//    ;


    RentType(int value, BigDecimal money, BigDecimal rate, BigDecimal profitMoney, BigDecimal profitMoneyExt, Integer days, String name,Integer digMin,Integer digMax) {
        this.value = value;
        this.money = money;
        this.rate = rate;
        this.profitMoney = profitMoney;
        this.profitMoneyExt = profitMoneyExt;
        this.days = days;
        this.name = name;
        this.digMin = digMin;
        this.digMax = digMax;
    }

    private int value;

    private BigDecimal money;
    private BigDecimal rate;
    private BigDecimal profitMoney;
    private BigDecimal profitMoneyExt;
    private Integer days;
    private String name;
    private Integer digMin;
    private Integer digMax;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
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

    public Integer getDigMin() {
        return digMin;
    }

    public void setDigMin(Integer digMin) {
        this.digMin = digMin;
    }

    public Integer getDigMax() {
        return digMax;
    }

    public void setDigMax(Integer digMax) {
        this.digMax = digMax;
    }

    public BigDecimal getProfitMoney() {
        return profitMoney;
    }

    public void setProfitMoney(BigDecimal profitMoney) {
        this.profitMoney = profitMoney;
    }

    public BigDecimal getProfitMoneyExt() {
        return profitMoneyExt;
    }

    public void setProfitMoneyExt(BigDecimal profitMoneyExt) {
        this.profitMoneyExt = profitMoneyExt;
    }

    public static RentType fromValue(int value){
        for (RentType type :RentType.values()) {
            if(type.getValue() == value){
                return type;
            }
        }
        return null;
    }
}
