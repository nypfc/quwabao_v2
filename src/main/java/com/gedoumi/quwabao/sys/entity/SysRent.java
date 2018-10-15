package com.gedoumi.quwabao.sys.entity;


import com.gedoumi.quwabao.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统短信
 */
@Entity
@Table(name = "sys_rent")
public class SysRent implements Serializable {


	private static final long serialVersionUID = -4130133564886271094L;
	private Long id;

	private BigDecimal money;
	private BigDecimal rate;
	private BigDecimal profitMoney;
	private BigDecimal profitMoneyExt;
	private Integer days = Constants.RENT_DAYS.intValue();
	private String name;
	private Integer digMin;
	private Integer digMax;
	private Integer maxNumber;
	private Date createTime;
	private Date updateTime;
	private Integer rentStatus;
	private String code;
	//已经租用的矿机数量
	private Integer rentNumber;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "money", nullable = false, precision = 20, scale = 5)
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	@Column(name = "rate", nullable = false, precision = 20, scale = 5)
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	@Column(name = "profit_money", nullable = false, precision = 20, scale = 5)
	public BigDecimal getProfitMoney() {
		return profitMoney;
	}

	public void setProfitMoney(BigDecimal profitMoney) {
		this.profitMoney = profitMoney;
	}

	@Column(name = "profit_money_ext", nullable = false, precision = 20, scale = 5)
	public BigDecimal getProfitMoneyExt() {
		return profitMoneyExt;
	}

	public void setProfitMoneyExt(BigDecimal profitMoneyExt) {
		this.profitMoneyExt = profitMoneyExt;
	}

	@Column(name = "days", nullable = false, precision = 20, scale = 5)
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	@Column(name = "name", nullable = false, precision = 20, scale = 5)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "dig_min", nullable = false, precision = 20, scale = 5)
	public Integer getDigMin() {
		return digMin;
	}

	public void setDigMin(Integer digMin) {
		this.digMin = digMin;
	}

	@Column(name = "dig_max", nullable = false, precision = 20, scale = 5)
	public Integer getDigMax() {
		return digMax;
	}

	public void setDigMax(Integer digMax) {
		this.digMax = digMax;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "max_number", length = 20)
	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}

	@Column(name = "rent_status", length = 2)
	public Integer getRentStatus() {
		return rentStatus;
	}

	public void setRentStatus(Integer rentStatus) {
		this.rentStatus = rentStatus;
	}

	@Column(name = "rent_code", length = 4)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "rent_number", length = 11)
	public Integer getRentNumber() {
		return rentNumber;
	}

	public void setRentNumber(Integer rentNumber) {
		this.rentNumber = rentNumber;
	}
}
