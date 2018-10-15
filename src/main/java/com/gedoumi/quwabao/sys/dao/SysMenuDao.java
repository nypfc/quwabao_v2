package com.gedoumi.quwabao.sys.dao;

import com.gedoumi.quwabao.sys.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMenuDao extends JpaRepository<SysMenu, Long> {

}
