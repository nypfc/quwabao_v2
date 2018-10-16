package com.gedoumi.quwabao.common.security;

import com.gedoumi.quwabao.common.AppConfig;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.user.dataobj.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Aspect
@Component
public class RepeatAspect {

    @Resource
    private RedisCache redisCache;

    @Resource
    private AppConfig appConfig;

    /**
     * 使用Pointcut定义切点
     */
    @Pointcut("@annotation(com.gedoumi.quwabao.common.annotation.PfcRepeatAspect)")
    private void myPointcut(){}

    @Around(value = "myPointcut()")
    public Object arround(ProceedingJoinPoint joinPoint){
        log.info(" in arroud ");
        User user = ContextUtil.getUserFromRequest();
        ResponseObject responseObject = new ResponseObject();
        AtomicInteger lock = appConfig.getUserLock(user.getMobilePhone());
        int lockNumber = lock.incrementAndGet();
        if(lockNumber > 1){
            if(StringUtils.equals(joinPoint.getSignature().getName(),"withdraw")){
                responseObject = new ResponseObject<>(CodeEnum.WithDrawTimesError);
            }
            if(StringUtils.equals(joinPoint.getSignature().getName(),"rent")){
                responseObject = new ResponseObject<>(CodeEnum.RentError);
            }

            lock.decrementAndGet();
            log.info("error , lock ={}", lock);
            return responseObject;
        }

        Object result;
        try {
             result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            lock.set(0);
            return new ResponseObject<>(CodeEnum.SysError);
        }
        lock.set(0);
        log.info(" exit arroud ,lock = {}", lock);
        return result;
    }

}
