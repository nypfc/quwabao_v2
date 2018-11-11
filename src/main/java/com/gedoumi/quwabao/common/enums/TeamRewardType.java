package com.gedoumi.quwabao.common.enums;


import java.math.BigDecimal;

public enum TeamRewardType {

    Level_1(1, new BigDecimal("1600000"), 320, new BigDecimal("0.04"), new BigDecimal("64000"), "160w&320台矿机"),
    Level_2(2, new BigDecimal("8000000"), 1600, new BigDecimal("0.03"), new BigDecimal("240000"),"800w&1600台矿机"),
    Level_3(3, new BigDecimal("16000000"), 3200, new BigDecimal("0.025"), new BigDecimal("400000"),"1600w&3200台矿机"),
//    ;

//    Level_1(1, new BigDecimal("40000"), 5, new BigDecimal("0.04"), new BigDecimal("1600"), "4w&5台矿机"),
//    Level_2(2, new BigDecimal("200000"), 25, new BigDecimal("0.03"), new BigDecimal("6000"),"20w&25台矿机"),
//    Level_3(3, new BigDecimal("400000"), 50, new BigDecimal("0.025"), new BigDecimal("10000"),"40w&50台矿机"),
    ;


    TeamRewardType(int value, BigDecimal asset, int rentNum, BigDecimal rate,BigDecimal reward, String name) {
        this.value = value;
        this.asset = asset;
        this.rentNum = rentNum;
        this.rate = rate;
        this.reward = reward;
        this.name = name;
    }

    private int value;
    private BigDecimal asset;//租用矿机资产门槛
    private int rentNum;//矿机数量
    private BigDecimal rate;//奖励比例
    private BigDecimal reward;//奖励金额
    private String name;


    public BigDecimal getAsset() {
        return asset;
    }

    public void setAsset(BigDecimal asset) {
        this.asset = asset;
    }

    public int getRentNum() {
        return rentNum;
    }

    public void setRentNum(int rentNum) {
        this.rentNum = rentNum;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
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

    public static TeamRewardType fromValue(int value){
        for (TeamRewardType type :TeamRewardType.values()) {
            if(type.getValue() == value){
                return type;
            }
        }
        return null;
    }
}
