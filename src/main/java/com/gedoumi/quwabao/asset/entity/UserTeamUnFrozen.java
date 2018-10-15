package com.gedoumi.quwabao.asset.entity;

import com.gedoumi.quwabao.user.dataobj.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 团队解锁信息
 */
@Entity
@Table(name = "user_team_un_frozen")
public class UserTeamUnFrozen implements Serializable {


    private static final long serialVersionUID = 6413057117505017001L;
    private Long id;

    private User user;

    private Integer unFrozenType;

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

    @Column(name = "un_frozen_type", nullable = false, length = 2)
    public Integer getUnFrozenType() {
        return unFrozenType;
    }

    public void setUnFrozenType(Integer unFrozenType) {
        this.unFrozenType = unFrozenType;
    }
}
