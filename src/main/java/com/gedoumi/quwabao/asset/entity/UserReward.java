package com.gedoumi.quwabao.asset.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 推荐人奖励信息
 */
@Entity
@Table(name = "user_reward")
public class UserReward implements Serializable {


    private static final long serialVersionUID = -7127980573827380226L;
    private Long id;

    private User user;
    private User rewardUser;

    //奖励额度
    private BigDecimal reward;
    //每日解冻的额度
    private BigDecimal unlockPerDay;
    //剩余解冻的额度
    private BigDecimal remainFrozen;

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

    @ManyToOne
    @JoinColumn(name = "reward_user_id", nullable = false)
    public User getRewardUser() {
        return rewardUser;
    }

    public void setRewardUser(User rewardUser) {
        this.rewardUser = rewardUser;
    }

    @Column(name = "reward", nullable = false, precision = 20, scale = 5)
    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
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
