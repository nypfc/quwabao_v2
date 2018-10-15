package com.gedoumi.quwabao.user.dataobj.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户--关联表
 */
@Entity
@Table(name = "user_image")
public class UserImage implements java.io.Serializable {

	private Long id;

	private Long userId;

	private String userImage;

	private String validateCode;

	private String message;

	private String score;

	private Date createTime;

	private Date updateTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "userId", unique = true, nullable = false)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "user_image",columnDefinition = "MEDIUMTEXT")
	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	@Column(name = "create_time")
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

	@Column(name = "validate_code", length = 8)
	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	@Column(name = "message", length = 200)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "score", length = 20)
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}