package com.gedoumi.quwabao.guess.websocket;

import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * WebSocket拦截器，用于WebSocket握手成功时进行额外的逻辑处理
 * @author Minced
 */
@Slf4j
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Resource
    private UserService userService;

    /**
     * 握手前执行的方法，验证用户Session，返回true继续握手，返回false中断握手
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // request参数强转
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        // 获取Token与设备ID
        String authToken = servletRequest.getParameter("auth-token");
        String deviceId = servletRequest.getParameter("deviceid");
        // 未获取到Token，握手失败
        if (authToken == null) {
            log.info("未获取到Token，握手失败");
            return false;
        }
        if (deviceId == null) {
            log.info("未获取到设备ID，握手失败");
            return false;
        }
        // 获取用户
        User user = userService.getByToken(authToken);
        // 未获取到用户，握手失败
        if (user == null) {
            log.info("未获取到用户，握手失败");
            return false;
        }
        // 传入的DeviceID与用户登录的DeviceID不同，握手失败
        if (!StringUtils.equals(deviceId, user.getDeviceId())) {
            log.info("{}用户已经在其他设备上登录", user.getMobilePhone());
            return false;
        }
        // 传递用户ID
        log.info("握手成功");
        attributes.put("userId", user.getId());
        return true;
    }

    /**
     * 握手后执行的方法
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
