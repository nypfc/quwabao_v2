package com.gedoumi.quwabao.asset.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 团队奖励信息
 * 根据团队矿机数量和币总量进行奖励
 * @See TeamRewardType
 */
@Entity
@Table(name = "user_team_reward")
public class UserTeamReward implements Serializable {


    private static final long serialVersionUID = 5419296231792134912L;
    private Long id;

    private User user;

    //奖励额度
    private BigDecimal reward;
    //冻结奖励的额度（初始冻结50%的奖励，保持不变）
    private BigDecimal frozenAsset;
    //每日解冻的额度
    private BigDecimal unlockPerDay;
    //剩余解冻的额度
    private BigDecimal remainFrozen;
    //奖励类型
    private Integer teamRewardType;

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

    @Column(name = "reward", nullable = false, precision = 20, scale = 5)
    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    @Column(name = "frozen_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getFrozenAsset() {
        return frozenAsset;
    }

    public void setFrozenAsset(BigDecimal frozenAsset) {
        this.frozenAsset = frozenAsset;
    }

    @Column(name = "unlock_perday", nullable = false, precision = 20, scale = 5)
    public BigDecimal getUnlockPerDay() {
        return unlockPerDay;
    }

    public void setUnlockPerDay(BigDecimal unlockPerDay) {
        this.unlockPerDay = unlockPerDay;
    }

    @Column(name = "remain_frozen", nullable = false, precision = 20, scale = 5)
    public BigDecimal getRemainFrozen() {
        return remainFrozen;
    }

    public void setRemainFrozen(BigDecimal remainFrozen) {
        this.remainFrozen = remainFrozen;
    }

    @Column(name = "team_reward_type", nullable = false, length = 2)
    public Integer getTeamRewardType() {
        return teamRewardType;
    }

    public void setTeamRewardType(Integer teamRewardType) {
        this.teamRewardType = teamRewardType;
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
