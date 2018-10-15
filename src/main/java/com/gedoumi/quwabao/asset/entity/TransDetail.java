package com.gedoumi.quwabao.asset.entity;

import com.gedoumi.quwabao.user.dataobj.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "trans_detail")
public class TransDetail implements Serializable {


    private static final long serialVersionUID = -4765989936506432675L;

    private Long id;

    private User fromUser;

    private User toUser;

    private BigDecimal money;

    private Integer transStatus;

    private Integer transDetailType;

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
    @JoinColumn(name = "from_user_id", nullable = false)
    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }


    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }


    @Column(name = "money", nullable = false, precision = 20, scale = 5)
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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

    @Column(name = "trans_status", nullable = false, length = 2)
    public Integer getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(Integer transStatus) {
        this.transStatus = transStatus;
    }

    @Column(name = "trans_detail_type", nullable = false, length = 2)
    public Integer getTransDetailType() {
        return transDetailType;
    }

    public void setTransDetailType(Integer transDetailType) {
        this.transDetailType = transDetailType;
    }
}
