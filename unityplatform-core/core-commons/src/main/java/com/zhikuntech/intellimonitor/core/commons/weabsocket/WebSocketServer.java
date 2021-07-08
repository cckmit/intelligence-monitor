package com.zhikuntech.intellimonitor.core.commons.weabsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Slf4j
@ServerEndpoint(value = "/websocket/{identityTag}")
@Component
@ConditionalOnProperty(prefix = "websocket" , havingValue = "true" ,value = "config")
public class WebSocketServer {

    /**
     * 记录当前在线连接数
     */
    public static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    public static ConcurrentHashMap<String, Session> clients = new ConcurrentHashMap<>();

    public static List<BaseWebSocketHandler> baseHandlers;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("identityTag") String identityTag) {
        onlineCount.incrementAndGet();
        put(session, identityTag);
        for (BaseWebSocketHandler baseHandler : baseHandlers) {
            baseHandler.onOpen(identityTag);
        }
        log.info("有新连接加入：{}，当前在线人数为：{}", identityTag, onlineCount.get());
    }

    private void put(Session session, String identityTag) {
        if(clients.containsKey(identityTag)){
            try {
                clients.get(identityTag).close();
            } catch (IOException e) {
                log.error("关闭session时出现异常{}",e.getMessage());
                e.printStackTrace();
            }
        }
        clients.put(identityTag, session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("identityTag") String identityTag) {
        onlineCount.decrementAndGet();
        clients.remove(identityTag);
        for (BaseWebSocketHandler baseHandler : baseHandlers) {
            baseHandler.onClose(identityTag);
        }
        log.info("有一连接关闭：{}，当前在线人数为：{}", identityTag, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("identityTag") String identityTag) {
        if ("hello".equals(message)) {
            log.info(message);
            sendMessage("world", identityTag);
            log.info("world");
        } else {
            for (BaseWebSocketHandler baseHandler : baseHandlers) {
                baseHandler.onMessage(message, identityTag);
            }
        }
    }

    @OnError
    public void onError(Throwable error, @PathParam("identityTag") String identityTag) {
        clients.remove(identityTag);
        for (BaseWebSocketHandler baseHandler : baseHandlers) {
            baseHandler.onError(identityTag);
        }
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message, String identityTag) {
        Session session = clients.get(identityTag);
        if (session == null) {
            log.error("No websocket session found for user: {}, message: {}.", identityTag, message);
            return;
        }
        synchronized (session) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("服务端发送消息给客户端失败：", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 分组推送消息
     *
     * @param message 推送消息内容
     * @param set     用户组
     */
    public void sendGroupMessage(String message, Set<String> set) {
        for (String username : set) {
            sendMessage(message, username);
        }
    }

    /**
     * 群发消息(所有人)
     *
     * @param message 消息内容
     */
    public void sendAllMessage(String message) {
        for (String username : clients.keySet()) {
            sendMessage(message, username);
        }
    }
}