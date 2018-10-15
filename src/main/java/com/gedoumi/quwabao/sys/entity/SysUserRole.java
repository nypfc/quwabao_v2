package com.gedoumi.quwabao.sys.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 用户--角色关联表对应实体
 */
@Entity
@Table(name = "sys_user_role")
public class SysUserRole implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long userId;
	private Long roleId;
	private Long createUserId;
	private Timestamp createTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "user_id", nullable = false, length = 32)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "role_id", nullable = false, length = 32)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "create_user_id")
	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}