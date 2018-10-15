package com.gedoumi.quwabao.sys.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 角色--权限关联
 */
@Entity
@Table(name = "sys_role_authority")
public class SysRoleAuthority implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long roleId; // 角色id
	private Long authId; // 权限id
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

	@Column(name = "role_id", nullable = false, length = 32)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "auth_id", nullable = false, length = 32)
	public Long getAuthId() {
		return this.authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
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