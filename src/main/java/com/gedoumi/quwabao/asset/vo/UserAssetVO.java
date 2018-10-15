package com.gedoumi.quwabao.asset.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserAssetVO implements Serializable {


    private static final long serialVersionUID = -8221961284698261430L;

    private BigDecimal totalAsset = BigDecimal.ZERO;
    private BigDecimal frozenAsset = BigDecimal.ZERO;

    private BigDecimal remainAsset = BigDecimal.ZERO;

    private BigDecimal profit = BigDecimal.ZERO;

    private BigDecimal rentProfit = BigDecimal.ZERO;

    private BigDecimal lastDayProfit = BigDecimal.ZERO;

    private int digNumber;

    private Integer rentType;

    private String rentName;

    private Integer onLine;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expireDate;

    private UserInfoVO user;

    public BigDecimal getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(BigDecimal totalAsset) {
        this.totalAsset = totalAsset;
    }

    public BigDecimal getRemainAsset() {
        return remainAsset;
    }

    public void setRemainAsset(BigDecimal remainAsset) {
        this.remainAsset = remainAsset;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getRentProfit() {
        return rentProfit;
    }

    public void setRentProfit(BigDecimal rentProfit) {
        this.rentProfit = rentProfit;
    }

    public BigDecimal getLastDayProfit() {
        return lastDayProfit;
    }

    public void setLastDayProfit(BigDecimal lastDayProfit) {
        this.lastDayProfit = lastDayProfit;
    }

    public int getDigNumber() {
        return digNumber;
    }

    public void setDigNumber(int digNumber) {
        this.digNumber = digNumber;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public UserInfoVO getUser() {
        return user;
    }

    public void setUser(UserInfoVO user) {
        this.user = user;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getOnLine() {
        return onLine;
    }

    public void setOnLine(Integer onLine) {
        this.onLine = onLine;
    }

    public BigDecimal getFrozenAsset() {
        return frozenAsset;
    }

    public void setFrozenAsset(BigDecimal frozenAsset) {
        this.frozenAsset = frozenAsset;
    }

    public String getRentName() {
        return rentName;
    }

    public void setRentName(String rentName) {
        this.rentName = rentName;
    }
}
