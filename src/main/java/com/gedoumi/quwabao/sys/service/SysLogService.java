package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.sys.dao.SysLogDao;
import com.gedoumi.quwabao.sys.entity.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class SysLogService {
	
	@Autowired
	private SysLogDao logDao;

	@Transactional
	public void add(SysLog sysLog){
		logDao.save(sysLog);
	}

	@Transactional
	public void update(SysLog sysLog){
		logDao.save(sysLog);
	}



}
