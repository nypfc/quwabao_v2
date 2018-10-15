package com.gedoumi.quwabao.sys.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 网关充值记录
 */
@Entity
@Table(name = "sys_log")
public class SysLog implements Serializable {


	private static final long serialVersionUID = 1955698071845558836L;
	private Long id;

	private Integer logStatus; //状态，1：可用，0：不可用
	private String requestUrl;
	private String requestBody;
	private String clientIp;
	private String seq;
	private Date createTime;
	private Date updateTime;
	private String mobile;
	private Integer logType;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "log_status", length = 2, nullable = false)
	public Integer getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(Integer logStatus) {
		this.logStatus = logStatus;
	}

	@Column(name = "request_url", length = 255, nullable = false)
	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Column(name = "request_body", length = 255, nullable = false)
	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	@Column(name = "client_ip", length = 50, nullable = false)
	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
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

	@Column(name = "trans_seq", length = 128)
	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	@Column(name = "mobile", length = 20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "log_type", length = 2)
	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}


}
