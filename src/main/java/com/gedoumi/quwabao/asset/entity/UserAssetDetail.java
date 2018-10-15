package com.gedoumi.quwabao.asset.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gedoumi.quwabao.user.dataobj.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "user_asset_detail")
public class UserAssetDetail implements Serializable {


    private static final long serialVersionUID = -4765989936506432675L;

    private Long id;

    @JsonIgnore
    private User user;

    private BigDecimal money;

    private BigDecimal profit;

    private BigDecimal profitExt;
    //手续费
    private BigDecimal fee;

    private Integer versionType;

    private Integer transType;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date updateTime;

    private UserRent userRent;

    //交易相关用户（转账用户或者推荐人奖励用户）
    private User rewardUser;

    private Date digDate;
    //网关接口调用唯一序列号
    private String apiTransSeq;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "reward_user_id", nullable = true)
    public User getRewardUser() {
        return rewardUser;
    }

    public void setRewardUser(User rewardUser) {
        this.rewardUser = rewardUser;
    }

    @ManyToOne
    @JoinColumn(name = "rent_id", nullable = true)
    public UserRent getUserRent() {
        return userRent;
    }

    public void setUserRent(UserRent userRent) {
        this.userRent = userRent;
    }

    @Column(name = "money", nullable = false, precision = 20, scale = 5)
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Column(name = "profit", nullable = false, precision = 20, scale = 5)
    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    @Column(name = "profit_ext", nullable = false, precision = 20, scale = 5)
    public BigDecimal getProfitExt() {
        return profitExt;
    }

    public void setProfitExt(BigDecimal profitExt) {
        this.profitExt = profitExt;
    }

    @Column(name = "trans_type", nullable = false)
    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    @Column(name = "create_time", nullable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time", nullable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @Column(name = "version_type", nullable = false)
    public Integer getVersionType() {
        return versionType;
    }

    public void setVersionType(Integer versionType) {
        this.versionType = versionType;
    }

    @Column(name = "dig_date", nullable = true)
    @Temporal(value= TemporalType.DATE)
    public Date getDigDate() {
        return digDate;
    }

    public void setDigDate(Date digDate) {
        this.digDate = digDate;
    }

    @Column(name = "api_trans_seq", nullable = true)
    public String getApiTransSeq() {
        return apiTransSeq;
    }

    public void setApiTransSeq(String apiTransSeq) {
        this.apiTransSeq = apiTransSeq;
    }

    @Column(name = "fee", nullable = true)
    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
