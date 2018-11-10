package com.pfc.quwabao.common.utils;

import com.pfc.quwabao.user.dataobj.model.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.pfc.quwabao.common.constants.Constants.API_USER_KEY;
import static com.pfc.quwabao.common.constants.Constants.AUTH_TOKEN;

/**
 * 上下文工具类
 *
 * @author Minced
 */
public class ContextUtil {

    /**
     * 获取HttpServletRequest对象
     *
     * @return HttpServletRequest对象
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取请求IP
     *
     * @return 请求IP
     */
    public static String getClientIp() {
        String remoteAddr;
        remoteAddr = getRequest().getHeader("X-FORWARDED-FOR");
        if (remoteAddr == null || "".equals(remoteAddr)) {
            remoteAddr = getRequest().getRemoteAddr();
        }
        return remoteAddr;
    }

    /**
     * 从Request作用域中获取令牌
     *
     * @return 令牌
     */
    public static String getTokenFromHead() {
        return getRequest().getHeader(AUTH_TOKEN);
    }

    /**
     * 从Request作用域中获取用户
     *
     * @return 用户对象
     */
    public static User getUserFromRequest() {
        return (User) getRequest().getAttribute(API_USER_KEY);
    }

}
