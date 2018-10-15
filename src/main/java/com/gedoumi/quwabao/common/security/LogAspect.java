package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.api.gateway.ApiResponse;
import com.gedoumi.quwabao.api.gateway.vo.RechargeVO;
import com.gedoumi.quwabao.common.enums.LogType;
import com.gedoumi.quwabao.common.enums.SysLogStatus;
import com.gedoumi.quwabao.sys.entity.SysLog;
import com.gedoumi.quwabao.sys.service.SysLogService;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.common.utils.SessionUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    static Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Resource
    private SysLogService logService;

    /**
     * 使用Pointcut定义切点
     */
    @Pointcut("@annotation(com.gedoumi.quwabao.common.annotation.PfcLogAspect)")
    private void myPointcut(){}

//    @Before(value = "myPointcut()")
    public void before(JoinPoint joinPoint){
        logger.info(" in before ");
        Object[] args = joinPoint.getArgs();
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(SessionUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.Recharge.getValue());
        sysLog.setRequestUrl(SessionUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(JsonUtil.objectToJson(args));
        if(args[0] instanceof RechargeVO){
            RechargeVO rechargeVO = (RechargeVO)args[0];
            sysLog.setMobile(rechargeVO.getPfc_account());
            sysLog.setSeq(rechargeVO.getSeq());
        }

        sysLog.setLogStatus(SysLogStatus.Init.getValue());
        logService.add(sysLog);
    }

    @AfterReturning(value = "myPointcut()", returning = "apiResponse")
    public void afterRunning(JoinPoint joinPoint, ApiResponse apiResponse){
        logger.info(" in afterRunning apiResponse = {}", JsonUtil.objectToJson(apiResponse));
        Object[] args = joinPoint.getArgs();
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(SessionUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.Recharge.getValue());
        sysLog.setRequestUrl(SessionUtil.getRequest().getRequestURI());
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

        logService.add(sysLog);
    }
}
