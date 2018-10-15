package com.gedoumi.quwabao.guess.websocket;

import com.gedoumi.quwabao.component.ApiInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * WebSocket配置类
 * @author Minced
 */
@Component
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WebSocketInterceptor webSocketInterceptor;
    @Resource
    private GuessWebSocketHandler guessWebSocketHandler;

    /**
     * 注册拦截器与事件处理器
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 正常连接的路径，注意跨域
        registry.addHandler(guessWebSocketHandler, "/guess/ws")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketInterceptor);
        // 如果浏览器不支持WebSocket，前端使用此路径模拟WebSocket的连接，注意跨域
        registry.addHandler(guessWebSocketHandler, "/guess/ws/sockjs")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketInterceptor)
                .withSockJS();
    }

    @Bean
    public ApiInterceptor apiInterceptor() {
        return new ApiInterceptor();
    }

}
