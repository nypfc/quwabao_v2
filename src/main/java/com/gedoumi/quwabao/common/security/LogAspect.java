package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.api.gateway.ApiResponse;
import com.gedoumi.quwabao.api.gateway.vo.RechargeVO;
import com.gedoumi.quwabao.common.enums.LogType;
import com.gedoumi.quwabao.common.enums.SysLogStatus;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.sys.dataobj.model.SysLog;
import com.gedoumi.quwabao.sys.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private SysLogService logService;

    /**
     * 使用Pointcut定义切点
     */
    @Pointcut("@annotation(com.gedoumi.quwabao.common.annotation.PfcLogAspect)")
    private void myPointcut(){}

//    @Before(value = "myPointcut()")
    public void before(JoinPoint joinPoint){
        log.info(" in before ");
        Object[] args = joinPoint.getArgs();
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(ContextUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.Recharge.getValue());
        sysLog.setRequestUrl(ContextUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(JsonUtil.objectToJson(args));
        if(args[0] instanceof RechargeVO){
            RechargeVO rechargeVO = (RechargeVO)args[0];
            sysLog.setMobile(rechargeVO.getPfc_account());
            sysLog.setSeq(rechargeVO.getSeq());
        }

        sysLog.setLogStatus(SysLogStatus.Init.getValue());
        logService.createSysLog(sysLog);
    }

    @AfterReturning(value = "myPointcut()", returning = "apiResponse")
    public void afterRunning(JoinPoint joinPoint, ApiResponse apiResponse){
        log.info(" in afterRunning apiResponse = {}", JsonUtil.objectToJson(apiResponse));
        Object[] args = joinPoint.getArgs();
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(ContextUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.Recharge.getValue());
        sysLog.setRequestUrl(ContextUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(JsonUtil.objectToJson(args));
        if(args[0] instanceof RechargeVO){
            RechargeVO rechargeVO = (RechargeVO)args[0];
            sysLog.setMobile(rechargeVO.getPfc_account());
            sysLog.setSeq(rechargeVO.getSeq());
        }
        if(apiResponse.getCode() == 0){
            sysLog.setLogStatus(SysLogStatus.Success.getValue());
        }else {
            sysLog.setLogStatus(SysLogStatus.Fail.getValue());
        }

        logService.createSysLog(sysLog);
    }
}
