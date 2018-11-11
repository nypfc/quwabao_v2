package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.sys.dataobj.model.SysLog;
import com.gedoumi.quwabao.sys.mapper.SysLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * 网关日志Service
 *
 * @author Minced
 */
@Service
public class SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * 创建网关日志
     *
     * @param sysLog 日志对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSysLog(SysLog sysLog) {
        sysLogMapper.createSysLog(sysLog);
    }

}
