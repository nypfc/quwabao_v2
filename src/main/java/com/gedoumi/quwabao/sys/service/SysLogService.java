package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.sys.dataobj.model.SysLog;
import com.gedoumi.quwabao.sys.mapper.SysLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

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
     * @param mobile        手机号
     * @param requestObject 请求对象
     * @param logType       日志类型
     * @param logStatus     日志状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSysLog(String mobile, Object requestObject, Integer logType, Integer logStatus) {
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(ContextUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(logType);
        sysLog.setRequestUrl(ContextUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(JsonUtil.objectToJson(requestObject));
        sysLog.setMobile(mobile);
        sysLog.setLogStatus(logStatus);
        sysLogMapper.createSysLog(sysLog);
    }

}
