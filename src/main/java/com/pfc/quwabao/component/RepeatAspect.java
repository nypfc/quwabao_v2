package com.pfc.quwabao.component;

import com.pfc.quwabao.common.enums.CodeEnum;
import com.pfc.quwabao.common.utils.ContextUtil;
import com.pfc.quwabao.common.utils.ResponseObject;
import com.pfc.quwabao.user.dataobj.model.User;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 防止重复提交的AOP
 */
@Slf4j
@Aspect
public class RepeatAspect {

    private Map<String, Object> configContext = Maps.newHashMap();

    /**
     * 使用Pointcut定义切点
     */
    @Pointcut("@annotation(com.pfc.quwabao.common.annotation.RepeatAspect)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object arround(ProceedingJoinPoint joinPoint) {
        log.info(" in arroud ");
        User user = ContextUtil.getUserFromRequest();
        ResponseObject responseObject = new ResponseObject();
        AtomicInteger lock = getUserLock(user.getMobilePhone());
        assert lock != null;
        if (lock.incrementAndGet() > 1) {
            if (StringUtils.equals(joinPoint.getSignature().getName(), "withdraw")) {
                responseObject = new ResponseObject<>(CodeEnum.WithDrawTimesError);
            }
            lock.decrementAndGet();
            log.info("error , lock = {}", lock);
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

    private synchronized AtomicInteger getUserLock(String mobile) {
        if (StringUtils.isEmpty(mobile))
            return null;
        Object obj = configContext.get(mobile + "lock");
        if (obj == null) {
            configContext.put(mobile + "lock", new AtomicInteger());
        }
        return (AtomicInteger) configContext.get(mobile + "lock");
    }

}
