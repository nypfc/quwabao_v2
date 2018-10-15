package com.gedoumi.quwabao.asset.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferVO implements Serializable {


    private static final long serialVersionUID = -3778681093702688022L;
    private Integer transType;

    private BigDecimal transMoney;

    private String fromMobile;

    private String toMobile;

    private String password;

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public BigDecimal getTransMoney() {
        return transMoney;
    }

    public void setTransMoney(BigDecimal transMoney) {
        this.transMoney = transMoney;
    }

    public String getFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(String fromMobile) {
        this.fromMobile = fromMobile;
    }

    public String getToMobile() {
        return toMobile;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
