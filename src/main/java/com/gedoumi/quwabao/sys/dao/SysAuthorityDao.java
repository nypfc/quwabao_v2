package com.gedoumi.quwabao.sys.dao;

import com.gedoumi.quwabao.sys.entity.SysAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAuthorityDao extends JpaRepository<SysAuthority, Long> {

	public List<SysAuthority> findByMenuId(Long menuId);
}
