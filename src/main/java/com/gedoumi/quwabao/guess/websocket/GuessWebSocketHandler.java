package com.gedoumi.quwabao.guess.websocket;

import com.gedoumi.quwabao.common.enums.GuessNotityTypeEnum;
import com.gedoumi.quwabao.guess.vo.GuessNotityVO;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket消息处理器，用于跟前端交互时触发的各种事件
 * @author Minced
 */
@Slf4j
@Component
public class GuessWebSocketHandler extends TextWebSocketHandler {

    /**
     * 用于存放所有握手完成的WebSocket
     */
    private static final Map<Long, WebSocketSession> userSocketSessionMap;

    static {
        userSocketSessionMap = new HashMap<>();
    }

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    public void afterConnectionEstablished(WebSocketSession session) {
        // 获取用户ID
        Long userId = (Long) session.getAttributes().get("userId");
        userSocketSessionMap.put(userId, session);
        log.info("用户ID: {} 已连接", userId);
        log.info("目前有{}个用户正在连接", userSocketSessionMap.size());
        // 握手成功后推送信息
        try {
            GuessNotityVO guessNotityVO = new GuessNotityVO();
            guessNotityVO.setType(GuessNotityTypeEnum.CONNECTION_SUCCESS.getType());
            guessNotityVO.setMessage(GuessNotityTypeEnum.CONNECTION_SUCCESS.getMessage());
            String resultJson = JsonUtil.objectToJson(guessNotityVO);
            assert resultJson != null;
            session.sendMessage(new TextMessage(resultJson));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        Long userId = (Long) session.getAttributes().get("userId");
        log.info("用户ID: {} 已退出", userId);
        userSocketSessionMap.remove(userId);
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) session.close();
        userSocketSessionMap.remove(session);
    }

    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给某个用户发送消息
     */
    public void sendMessageToUser(Long userId, TextMessage message) {
        WebSocketSession user = userSocketSessionMap.get(userId);
        try {
            if (user.isOpen()) {
                user.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给所有在线用户发送消息
     */
    public void sendMessageToUsers(TextMessage message) {
        for (Map.Entry<Long, WebSocketSession> entry : userSocketSessionMap.entrySet()) {
            try {
                WebSocketSession user = entry.getValue();
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
