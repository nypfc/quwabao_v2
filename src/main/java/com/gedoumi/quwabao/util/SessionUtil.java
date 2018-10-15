package com.gedoumi.quwabao.util;

import com.gedoumi.quwabao.common.Constants;
import com.gedoumi.quwabao.common.MySessionContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionUtil {

    public static HttpSession getSession(){
        HttpSession httpSession = MySessionContext.getSession(getSessionIdFromHead());
        if(httpSession != null){
            return httpSession;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static AtomicInteger getLock(){
        Object obj = getSession().getAttribute(Constants.LOCK);
        if(obj == null){
            AtomicInteger atomicInteger = new AtomicInteger();
            getSession().setAttribute(Constants.LOCK, atomicInteger);
            return atomicInteger;
        }
        return (AtomicInteger)obj;
    }


    public static String getClientIp() {

        String remoteAddr = "";

        if (getRequest() != null) {
            remoteAddr = getRequest().getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = getRequest().getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    public static String getTokenFromHead() {

        String token = "";

        if (getRequest() != null) {
            token = getRequest().getHeader("auth-token");
        }

        return token;
    }

    public static String getSessionIdFromHead() {

        String sessionId = "";

        if (getRequest() != null) {
            sessionId = getRequest().getHeader("pfcsessionId");
        }

        return sessionId;
    }

    public static String getDeviceFromHead() {

        String deviceid = "";

        if (getRequest() != null) {
            deviceid = getRequest().getHeader("deviceid");
        }

        return deviceid;
    }

}
