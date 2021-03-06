package com.gedoumi.quwabao.common.component;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.gedoumi.quwabao.common.constants.Constants.API_USER_KEY;
import static com.gedoumi.quwabao.common.constants.Constants.AUTH_TOKEN;

/**
 * API拦截器
 *
 * @author Minced
 */
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Resource
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取设备ID与令牌
        log.info("RequestURI:{}", request.getRequestURI());
        String authToken = request.getHeader(AUTH_TOKEN);
        log.info("AuthToken:{}", authToken);
        // 判断令牌有效性
        if (StringUtils.isEmpty(authToken))
            throw new BusinessException(CodeEnum.EmptyToken);
        // 从缓存中获取用户
        User user = Optional.ofNullable((User) redisCache.getKeyValueData(authToken))
                .orElseGet(() -> Optional.ofNullable(userService.getByToken(authToken)).orElseThrow(() -> {
                    log.error("token:{}未查询到用户", authToken);
                    return new BusinessException(CodeEnum.InvalidToken);
                }));
        // 重新缓存用户（失效时间1小时）
        redisCache.setExpireKeyValueData(authToken, user, 1L, TimeUnit.HOURS);
        // 用户存入Request作用域
        request.setAttribute(API_USER_KEY, user);
        return true;
    }

}
