package com.gedoumi.quwabao.asset.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class RentTypeVO implements Serializable {


    private static final long serialVersionUID = -7562188232745413454L;

    private int rentType;

    private String rentName;

    private BigDecimal rentMoney;

    private BigDecimal rate;

    private BigDecimal profitMoney;

    private int days;

    private boolean rentFlag;

    public int getRentType() {
        return rentType;
    }

    public void setRentType(int rentType) {
        this.rentType = rentType;
    }

    public String getRentName() {
        return rentName;
    }

    public void setRentName(String rentName) {
        this.rentName = rentName;
    }

    public BigDecimal getRentMoney() {
        return rentMoney;
    }

    public void setRentMoney(BigDecimal rentMoney) {
        this.rentMoney = rentMoney;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getProfitMoney() {
        return profitMoney;
    }

    public void setProfitMoney(BigDecimal profitMoney) {
        this.profitMoney = profitMoney;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isRentFlag() {
        return rentFlag;
    }

    public void setRentFlag(boolean rentFlag) {
        this.rentFlag = rentFlag;
    }
}
