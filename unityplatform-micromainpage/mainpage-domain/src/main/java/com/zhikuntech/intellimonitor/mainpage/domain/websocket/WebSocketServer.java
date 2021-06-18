package com.zhikuntech.intellimonitor.mainpage.domain.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Slf4j
@ServerEndpoint(value = "/websocket/{username}")
@Component
public class WebSocketServer {


    /**
     * 记录当前在线连接数
     */
    public static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    public static ConcurrentHashMap<String, Session> clients = new ConcurrentHashMap<>();

    private Session session;

    private String username;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        onlineCount.incrementAndGet();
        this.session = session;
        this.username = username;
        clients.put(this.username, this.session);
        log.info("有新连接加入：{}，当前在线人数为：{}", this.username, onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        onlineCount.decrementAndGet();
        clients.remove(username);
        log.info("有一连接关闭：{}，当前在线人数为：{}", username, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
//        log.info("服务端收到客户端[{}]的消息:{}", username, message);
//        this.sendMessage("Hello, " + message, username);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message, String username) {
        lock.lock();
        try {
            Session session = clients.get(username);
            if (null != session) {
//                log.info("服务端给客户端[{}]发送消息{}", username, message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：", e);
        } finally {
            lock.unlock();
        }
    }

    public void sendAllMessage(String message) {
        for (Session session : clients.values()) {
//            log.info("服务端给客户端[{}]发送消息{}", username, message);
            session.getAsyncRemote().sendText(message);
        }
    }
}