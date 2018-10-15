package com.gedoumi.quwabao.user.dataobj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gedoumi.quwabao.common.enums.UserValidateStatus;
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
@Table(name = "user")
public class User implements Serializable {


	private static final long serialVersionUID = 6063475844423366017L;
//	@JsonIgnore
	private Long id;

//	@Pattern(regexp="^\\w{3,15}$",message="账号只能由数字、字母或者下划线组成，长度在3-15之间。")
	private String username; //用户名
	@NotBlank(message="密码不能为空")
	@JsonIgnore
	private String password; //密码
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss"  ,timezone="GMT+8")
	private Date registerTime; //注册时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
	private Date lastLoginTime; //最后登录时间
	@JsonIgnore
	private String lastLoginIp; //最后登录IP
	@JsonIgnore
	private Date errorTime; //登陆时密码输入错误时间(最后一次)
	@JsonIgnore
	private Short errorCount=0; //密码输入错误次数，当输入正确一次时清零
	@NotNull(message="状态不能为空")
	@Range(min=0,max=1,message="状态格式错误")
	private Integer userStatus; //用户状态，1：可用，0：不可用，禁止登陆系统

	@Length(min=2,max=15,message="真实姓名只能由汉字、数字、字母或者下划线组成，长度在2-10之间。")
	private String realName; //用户真实姓名
	@Pattern(regexp="^|\\d{11}$",message="手机号格式错误")
	private String mobilePhone; //手机号
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
	private Date updateTime;
	private String inviteCode;//邀请码
	private String regInviteCode;//注册的受邀请码
	@JsonIgnore
	private String token;
	@JsonIgnore
	private String deviceId;

	private Integer userType;

	private Integer sex = 1;

	private String ethAddress;

	private Integer validateStatus = UserValidateStatus.Init.getValue();

	private String idCard;

    public static final String PREFIX = "挖宝客";
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

	@Column(name = "mobile_phone", length = 11, nullable = false)
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

    @Column(name = "invite_code", length = 10)
    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

	@Column(name = "reg_invite_code", length = 10)
	public String getRegInviteCode() {
		return regInviteCode;
	}

	public void setRegInviteCode(String regInviteCode) {
		this.regInviteCode = regInviteCode;
	}

	@Column(name = "token", length = 36)
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "deviceId", length = 20)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "user_type", length = 2, nullable = false)
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Column(name = "sex", length = 2)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "eth_address", length = 60)
	public String getEthAddress() {
		return ethAddress;
	}

	public void setEthAddress(String ethAddress) {
		this.ethAddress = ethAddress;
	}

	@Column(name = "validate_status", length = 2)
	public Integer getValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(Integer validateStatus) {
		this.validateStatus = validateStatus;
	}

	@Column(name = "id_card", length = 18)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
}
