package com.example.spring.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/websocket/{userId}")
@Component("myWebSocketServer")
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 当前在线连接数
     */
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 用来存放每个客户端对应的 WebSocketServer 对象
     */
    private static final ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();
    private Session session;
    /**
     * 接收 userId
     */
    private String userId = "";

    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.userId = userId;
        this.session = session;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, session);
        } else {
            webSocketMap.put(userId, session);
            addOnlineCount();
        }
        log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
        sendMessage("连接成功！");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("userId:" + userId + ",message:" + message);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String userId, String message) {
        Session session = webSocketMap.get(userId);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send2AllUser(String message) {
        for (Map.Entry<String, Session> sessionEntry : webSocketMap.entrySet()) {
            sendMessage(sessionEntry.getKey(), message);
        }
    }
}