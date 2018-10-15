package com.gedoumi.quwabao.sys.dao;

import com.gedoumi.quwabao.sys.entity.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogDao extends JpaRepository<SysLog, Long> {

}
