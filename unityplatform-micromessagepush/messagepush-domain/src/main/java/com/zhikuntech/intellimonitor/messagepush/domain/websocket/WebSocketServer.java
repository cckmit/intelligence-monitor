package com.zhikuntech.intellimonitor.messagepush.domain.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 滕楠
 * @className WebSocketServer
 * @create 2021/7/7 17:13
 **/
@Slf4j
public class WebSocketServer {

    /**
     * 记录当前在线连接数
     */
    public static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    public static ConcurrentHashMap<String, Session> clients = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Session> groupRuntime = new ConcurrentHashMap<>();

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
        groupRuntime.remove(username);
        log.info("有一连接关闭：{}，当前在线人数为：{}", username, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {

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
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：", e);
        } finally {
            lock.unlock();
        }
    }


    public void sendAllMessage(String message) {
        for (Session session : groupRuntime.values()) {
            log.info("{}订阅fanscada数据", session.getId());
            lock.lock();
            try {
                session.getAsyncRemote().sendText(message);
            } finally {
                lock.unlock();
            }
        }
    }

}