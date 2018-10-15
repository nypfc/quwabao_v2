package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.common.AppConfig;
import com.gedoumi.quwabao.common.Constants;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.user.dataobj.entity.User;
import com.gedoumi.quwabao.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class RepeatAspect {
    static Logger logger = LoggerFactory.getLogger(RepeatAspect.class);

    @Resource
    private AppConfig appConfig;

    /**
     * 使用Pointcut定义切点
     */
    @Pointcut("@annotation(com.gedoumi.quwabao.common.annotation.PfcRepeatAspect)")
    private void myPointcut(){}

    @Around(value = "myPointcut()")
    public Object arround(ProceedingJoinPoint joinPoint){
        logger.info(" in arroud ");
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        ResponseObject responseObject = new ResponseObject();
        AtomicInteger lock = appConfig.getUserLock(user.getMobilePhone());
        int lockNumber = lock.incrementAndGet();
        if(lockNumber > 1){
            if(StringUtils.equals(joinPoint.getSignature().getName(),"withdraw")){
                responseObject.setInfo(CodeEnum.WithDrawTimesError);
            }
            if(StringUtils.equals(joinPoint.getSignature().getName(),"rent")){
                responseObject.setInfo(CodeEnum.RentError);
            }

            lock.decrementAndGet();
            logger.info("error , lock ={}", lock);
            return responseObject;
        }

        Object result = null;
        try {
             result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            responseObject.setInfo(CodeEnum.SysError);
            lock.set(0);
            return responseObject;
        }
        lock.set(0);
        logger.info(" exit arroud ,lock = {}", lock);
        return (ResponseObject)result;
    }

}
