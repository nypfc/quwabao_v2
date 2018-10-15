package com.gedoumi.quwabao.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表对应实体
 */
@Entity
@Table(name = "sys_user")
public class SysUser implements Serializable {


	private static final long serialVersionUID = 6064992992434033013L;
	@JsonIgnore
	private Long id;

//	@Pattern(regexp="^\\w{3,15}$",message="账号只能由数字、字母或者下划线组成，长度在3-15之间。")
	private String username; //用户名
	@NotBlank(message="密码不能为空")
	@JsonIgnore
	private String password; //密码
	private Date registerTime; //注册时间
	private Date lastLoginTime; //最后登录时间
	private String lastLoginIp; //最后登录IP
	private Date errorTime; //登陆时密码输入错误时间(最后一次)
	private Short errorCount; //密码输入错误次数，当输入正确一次时清零
	@NotNull(message="状态不能为空")
	@Range(min=0,max=1,message="状态格式错误")
	private Integer userStatus; //用户状态，1：可用，0：不可用，禁止登陆系统

	@Length(min=2,max=15,message="真实姓名只能由汉字、数字、字母或者下划线组成，长度在2-10之间。")
	private String realName; //用户真实姓名
	@Pattern(regexp="^|\\d{11}$",message="手机号格式错误")
	private String mobilePhone; //手机号
	private Date updateTime;

    public static final String PREFIX = "某某宝";
	public static final String PWD_INIT = "111111";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "username", length = 15, nullable = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 50, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "register_time", length = 19)
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Column(name = "last_login_time", length = 19)
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "last_login_ip",	length = 50)
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Column(name = "error_time",	length = 19)
	public Date getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(Date errorTime) {
		this.errorTime = errorTime;
	}

	@Column(name = "error_count")
	public Short getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Short errorCount) {
		this.errorCount = errorCount;
	}

	@Column(name = "user_status", nullable = false, length = 2)
	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	@Column(name = "real_name", nullable = true, length = 15)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Column(name = "mobile_phone", length = 11, nullable = true)
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}



}
