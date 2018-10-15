package com.gedoumi.quwabao.sys.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 权限表对应实体
 */
@Entity
@Table(name = "sys_authority")
public class SysAuthority implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long menuId; // 关联菜单id
	@NotNull(message = "权限序号不能为空")
	@Range(min = 1, max = 999, message = "权限序号必须为1-999的整数")
	private Short authSort; // 排序号
	@NotBlank(message = "权限名称不能为空")
	@Length(min = 1, max = 50, message = "权限名称长度不能大于50。")
	private String authName; // 权限名称
	@Length(min = 0, max = 500, message = "url长度不能大于500.")
	private String authUrl; // 权限url，多个用，连接
	private Long createUserId;
	private Timestamp createTime;
	private Long updateUserId;
	private Timestamp updateTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "menu_id", nullable = false)
	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@Column(name = "auth_sort", nullable = false)
	public Short getauthSort() {
		return this.authSort;
	}

	public void setauthSort(Short authSort) {
		this.authSort = authSort;
	}

	@Column(name = "auth_name", nullable = false, length = 50)
	public String getauthName() {
		return this.authName;
	}

	public void setauthName(String authName) {
		this.authName = authName;
	}

	@Column(name = "auth_url", length = 500)
	public String getauthUrl() {
		return this.authUrl;
	}

	public void setauthUrl(String authUrl) {
		this.authUrl = authUrl;
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

	@Column(name = "update_user_id")
	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}