package com.gedoumi.quwabao.sys.dao;

import com.gedoumi.quwabao.sys.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleDao extends JpaRepository<SysUserRole, Long> {

	public SysUserRole findByUserIdAndRoleId(Long userId, Long roleId);
	
	public List<SysUserRole> findByUserId(Long userId);
	
	public List<SysUserRole> findByRoleId(Long roleId);
	
	@Query(value = "select ur from SysUserRole ur left join SysUser u on u.id = ur.userId where u.username = ?")
	public List<SysUserRole> findByUsername(String username);
}
