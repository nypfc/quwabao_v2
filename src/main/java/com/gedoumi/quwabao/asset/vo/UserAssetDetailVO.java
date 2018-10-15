package com.gedoumi.quwabao.asset.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gedoumi.quwabao.asset.entity.UserRent;
import com.gedoumi.quwabao.user.dataobj.entity.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserAssetDetailVO implements Serializable {


    private static final long serialVersionUID = 3915727228679646370L;
    private Long id;

    private BigDecimal money;

    private BigDecimal profit;

    private BigDecimal profitExt;

    private BigDecimal fee;

    private Integer versionType;

    private Integer transType;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",  timezone="GMT+8")
    private Date updateTime;

    private UserRent userRent;

    private Integer digNumber;

    //交易相关用户（转账用户或者推荐人奖励用户）
    private User rewardUser;

    private User user;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date digDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getRewardUser() {
        return rewardUser;
    }

    public void setRewardUser(User rewardUser) {
        this.rewardUser = rewardUser;
    }

    public UserRent getUserRent() {
        return userRent;
    }

    public void setUserRent(UserRent userRent) {
        this.userRent = userRent;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfitExt() {
        return profitExt;
    }

    public void setProfitExt(BigDecimal profitExt) {
        this.profitExt = profitExt;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersionType() {
        return versionType;
    }

    public void setVersionType(Integer versionType) {
        this.versionType = versionType;
    }

    public Date getDigDate() {
        return digDate;
    }

    public void setDigDate(Date digDate) {
        this.digDate = digDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getDigNumber() {
        return digNumber;
    }

    public void setDigNumber(Integer digNumber) {
        this.digNumber = digNumber;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
