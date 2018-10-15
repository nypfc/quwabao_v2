package com.gedoumi.quwabao.asset.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "user_asset")
public class UserAsset implements Serializable {


    private static final long serialVersionUID = -4765989936506432675L;

    @JsonIgnore
    private Long id;

    private User user;
    //总额
    private BigDecimal totalAsset;
    //余额
    private BigDecimal remainAsset;
    //天使钻
    private BigDecimal frozenAsset;
    //上线交易所后，初始的解冻资产
    private BigDecimal initFrozenAsset = BigDecimal.ZERO;
    //上线交易所后，初始的业绩基数,本人及所有下线天使钻之和
    private BigDecimal initBaseAsset = BigDecimal.ZERO;
    //累计收益
    private BigDecimal profit;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date updateTime;


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

    @Column(name = "total_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(BigDecimal totalAsset) {
        this.totalAsset = totalAsset;
    }

    @Column(name = "remain_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getRemainAsset() {
        return remainAsset;
    }

    public void setRemainAsset(BigDecimal remainAsset) {
        this.remainAsset = remainAsset;
    }

    @Column(name = "frozen_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getFrozenAsset() {
        return frozenAsset;
    }

    public void setFrozenAsset(BigDecimal frozenAsset) {
        this.frozenAsset = frozenAsset;
    }

    @Column(name = "init_frozen_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getInitFrozenAsset() {
        return initFrozenAsset;
    }

    public void setInitFrozenAsset(BigDecimal initFrozenAsset) {
        this.initFrozenAsset = initFrozenAsset;
    }

    @Column(name = "profit", nullable = false, precision = 20, scale = 5)
    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    @Column(name = "init_base_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getInitBaseAsset() {
        return initBaseAsset;
    }

    public void setInitBaseAsset(BigDecimal initBaseAsset) {
        this.initBaseAsset = initBaseAsset;
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
}
