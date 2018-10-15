package com.gedoumi.quwabao.sys.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 菜单表对应实体
 */
@Entity
@Table(name = "sys_menu")
public class SysMenu implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull(message = "上级菜单不能为空")
	private Long superId; // 上级菜单id
	@NotNull(message = "状态不能为空")
	@Range(min = 0, max = 1, message = "状态格式错误")
	private Short menuStatus; // 菜单状态 是否可用，1:可用，0：不可用
	@NotNull(message = "菜单序号不能为空")
	@Range(min = 1, max = 999, message = "菜单序号必须为1-999的整数")
	private Short menuSort; // 排序号
	@NotBlank(message = "菜单名称不能为空")
	@Length(min = 1, max = 20, message = "菜单名称长度必须在1-20之间")
	private String menuName; // 菜单名称
	@Length(min = 0, max = 255, message = "url长度不能大于255.")
	private String menuUrl; // 菜单url
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

	@Column(name = "super_id")
	public Long getSuperId() {
		return this.superId;
	}

	public void setSuperId(Long superId) {
		this.superId = superId;
	}

	@Column(name = "menu_status")
	public Short getMenuStatus() {
		return this.menuStatus;
	}

	public void setMenuStatus(Short menuStatus) {
		this.menuStatus = menuStatus;
	}

	@Column(name = "menu_sort")
	public Short getMenuSort() {
		return this.menuSort;
	}

	public void setMenuSort(Short menuSort) {
		this.menuSort = menuSort;
	}

	@Column(name = "menu_name", length = 50)
	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Column(name = "menu_url", length = 50)
	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
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

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysMenu sysMenu = (SysMenu) o;

        if (!id.equals(sysMenu.id)) return false;
        return menuName.equals(sysMenu.menuName);

    }

    @Override
    public int hashCode() {
    	int result = 0;
    	if(id != null)
    		result = id.hashCode();
        result = 31 * result + menuName.hashCode();
        return result;
    }
}