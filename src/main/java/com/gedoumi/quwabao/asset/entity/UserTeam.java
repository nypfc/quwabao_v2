package com.gedoumi.quwabao.asset.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 团队信息
 * 由团队长领取低成本的初始币，币总量就是业绩基数，根据团队租赁矿机的币的总量作为比较业绩的条件,对初始币进行解锁
 * @See UnFrozenType
 */
@Entity
@Table(name = "team")
public class UserTeam implements Serializable {


    private static final long serialVersionUID = -6425394496187906100L;
    private Long id;

    private User user;

    //业绩基数
    private BigDecimal baseAsset;
    //已经解锁的资产,废弃
    private BigDecimal unFrozenAsset;
    //已经申请解锁的比例
    private BigDecimal unFrozenRate = BigDecimal.ZERO;

    //总租赁资产
    private BigDecimal totalRentAsset;
    //总矿机数量
    private Integer totalCount;

    private Integer teamStatus;

    private Date createTime;

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


    @Column(name = "base_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(BigDecimal baseAsset) {
        this.baseAsset = baseAsset;
    }

    @Column(name = "un_frozen_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getUnFrozenAsset() {
        return unFrozenAsset;
    }

    public void setUnFrozenAsset(BigDecimal unFrozenAsset) {
        this.unFrozenAsset = unFrozenAsset;
    }

    @Column(name = "un_frozen_rate", nullable = false, precision = 20, scale = 5)
    public BigDecimal getUnFrozenRate() {
        return unFrozenRate;
    }

    public void setUnFrozenRate(BigDecimal unFrozenRate) {
        this.unFrozenRate = unFrozenRate;
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

    @Column(name = "team_status", nullable = false, length = 2)
    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    @Column(name = "total_rent_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getTotalRentAsset() {
        return totalRentAsset;
    }

    public void setTotalRentAsset(BigDecimal totalRentAsset) {
        this.totalRentAsset = totalRentAsset;
    }


    @Column(name = "total_count", nullable = false)
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
