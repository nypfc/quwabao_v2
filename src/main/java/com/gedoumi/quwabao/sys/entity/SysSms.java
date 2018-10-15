package com.gedoumi.quwabao.sys.entity;


import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统短信
 */
@Entity
@Table(name = "sys_sms")
public class SysSms implements Serializable {


	private static final long serialVersionUID = 7250695126276333053L;
	private Long id;

	private Integer smsStatus; //状态，1：可用，0：不可用
	private Integer smsType; //类型，1：登录，0：注册

	@Pattern(regexp="^|\\d{11}$",message="手机号格式错误")
	private String mobilePhone; //手机号
	private Date createTime;
	private Date updateTime;
	private String code;//验证码


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "sms_status", length = 11, nullable = false)
	public Integer getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(Integer smsStatus) {
		this.smsStatus = smsStatus;
	}

	@Column(name = "sms_type", length = 2, nullable = false)
	public Integer getSmsType() {
		return smsType;
	}

	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

    @Column(name = "code", length = 10)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
