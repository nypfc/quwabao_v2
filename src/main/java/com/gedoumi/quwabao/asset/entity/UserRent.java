package com.gedoumi.quwabao.asset.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gedoumi.quwabao.user.dataobj.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "user_rent")
public class UserRent implements Serializable {


    private static final long serialVersionUID = 5677692820921088738L;
    private Long id;

    private User user;

    private BigDecimal rentAsset;

    private Integer rentType;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    private Date updateTime;

    private Integer days;
    //到期时间
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expireDate;
    //算力
    private Integer digNumber;

    private Integer rentStatus;

    private Integer firstRentType;


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


    @Column(name = "rent_asset", nullable = false, precision = 20, scale = 5)
    public BigDecimal getRentAsset() {
        return rentAsset;
    }

    public void setRentAsset(BigDecimal rentAsset) {
        this.rentAsset = rentAsset;
    }

    @Column(name = "days", nullable = false)
    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    @Column(name = "expire_date", nullable = false)
    @Temporal(value= TemporalType.DATE)
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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

    @Column(name = "rent_type", nullable = false)
    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    @Column(name = "dig_number", nullable = false)
    public Integer getDigNumber() {
        return digNumber;
    }

    public void setDigNumber(Integer digNumber) {
        this.digNumber = digNumber;
    }

    @Column(name = "rent_status", nullable = false, length = 2)
    public Integer getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Integer rentStatus) {
        this.rentStatus = rentStatus;
    }

    @Column(name = "first_rent_type", nullable = false, length = 1)
    public Integer getFirstRentType() {
        return firstRentType;
    }

    public void setFirstRentType(Integer firstRentType) {
        this.firstRentType = firstRentType;
    }
}
