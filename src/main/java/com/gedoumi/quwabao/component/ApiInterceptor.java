package com.gedoumi.quwabao.component;

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
    private RedisCache redisCache;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取设备ID与令牌
        log.info("RequestURI: {}", request.getRequestURI());
        String authToken = request.getHeader(AUTH_TOKEN);
        log.info("AuthToken: {}", authToken);
        // 判断令牌有效性
        if (StringUtils.isEmpty(authToken))
            throw new BusinessException(CodeEnum.EmptyTokenOrDeviceId);
        User user = (User) redisCache.getKeyValueData(authToken);
        // 如果未能从缓存中获取到用户，则从数据库中获取用户
        if (user == null) {
            user = Optional.ofNullable(userService.getByToken(authToken)).orElseThrow(() -> {
                log.error("token:{}未查询到用户", authToken);
                return new BusinessException(CodeEnum.InvalidToken);
            });
        }
        // 存入Redis缓存
        redisCache.setKeyValueData(authToken, user);
        // 用户存入Request作用域
        request.setAttribute(API_USER_KEY, user);
        return true;
    }

}
