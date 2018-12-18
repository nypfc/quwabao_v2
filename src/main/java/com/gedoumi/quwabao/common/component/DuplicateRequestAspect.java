package com.gedoumi.quwabao.common.component;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.user.dataobj.model.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 防止重复提交AOP
 *
 * @author Minced
 */
@Slf4j
@Component
@Aspect
public class DuplicateRequestAspect {

    @Resource
    private RedisCache redisCache;

    /**
     * 使用Pointcut定义切点
     */
    @Pointcut("@annotation(com.gedoumi.quwabao.common.annotation.DuplicateRequest)")
    private void pointcut() {
    }

    /**
     * 环绕切点
     *
     * @param joinPoint 切点包装类
     * @return Object
     * @throws Throwable 异常
     */
    @Around("pointcut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        String mobile = user.getMobilePhone();
        // 获取key：手机号 + 方法名
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        String key = mobile + ":" + methodName;
        setCache(mobile, key, methodName);  // 设置缓存
        try {
            // 执行切点
            Object result = joinPoint.proceed();
            // 执行完成删除缓存
            redisCache.deleteKeyValueData(key);
            return result;
        } catch (Throwable t) {
            // 出现异常删除缓存，再将异常重新抛出以便全局异常处理器捕获
            redisCache.deleteKeyValueData(key);
            throw t;
        }
    }

    /**
     * 设置缓存
     *
     * @param mobile     手机号
     * @param key        key
     * @param methodName 方法名
     */
    private synchronized void setCache(String mobile, String key, String methodName) {
        // 如果获取到缓存，说明上次请求未完成，抛出异常
        Optional.ofNullable(redisCache.getKeyValueData(key)).ifPresent(value -> {
            log.error("手机号:{}重复请求", mobile);
            throw new BusinessException(CodeEnum.DuplicateRequest);
        });
        // 设置缓存
        redisCache.setKeyValueData(key, methodName);
    }

}
