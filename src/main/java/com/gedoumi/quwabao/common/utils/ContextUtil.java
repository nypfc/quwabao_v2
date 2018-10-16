package com.gedoumi.quwabao.common.utils;

import com.gedoumi.quwabao.user.dataobj.model.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.gedoumi.quwabao.common.constants.Constants.API_USER_KEY;

public class ContextUtil {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getClientIp() {
        String remoteAddr;
        remoteAddr = getRequest().getHeader("X-FORWARDED-FOR");
        if (remoteAddr == null || "".equals(remoteAddr)) {
            remoteAddr = getRequest().getRemoteAddr();
        }
        return remoteAddr;
    }

    public static String getTokenFromHead() {
        return getRequest().getHeader("auth-token");
    }

    public static String getDeviceFromHead() {
        return getRequest().getHeader("deviceid");
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
