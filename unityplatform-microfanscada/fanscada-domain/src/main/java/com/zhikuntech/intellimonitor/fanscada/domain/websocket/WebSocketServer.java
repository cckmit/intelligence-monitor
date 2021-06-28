package com.zhikuntech.intellimonitor.fanscada.domain.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.SocketParam;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.LoopVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
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
    public static ConcurrentHashMap<String, Session> group = new ConcurrentHashMap<>();

    private Session session;

    private String username;

    private ReentrantLock lock = new ReentrantLock();

    public static GoldenUtil goldenUtil;
    public static FanIndexService fanIndexService;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        onlineCount.incrementAndGet();
        this.session = session;
        this.username = username;
        clients.put(this.username, this.session);

        /**
         * 第一次有人连接时,开始获取实时数据
         */
        if (clients.size() == 1) {
            fanIndexService.getFanBaseInfoList(this.username);
        }
        log.info("有新连接加入：{}，当前在线人数为：{}", this.username, onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        onlineCount.decrementAndGet();
        //clients.remove(username);
        group.remove(username);
        log.info("有一连接关闭：{}，当前在线人数为：{}", username, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        SocketParam socketParam = JSON.parseObject(message, SocketParam.class);
        String messageType = socketParam.getMessageType();
        String userMessage = socketParam.getMessage();
        if (messageType.contains("01")){
            //规定数据格式,解析以校验权限,分组,等.
            log.info("接收到{}的消息,内容{}", username, userMessage);
            List<LoopVO> fanBaseInfoList = fanIndexService.getFanBaseInfoList();
            String jsonString = JSONObject.toJSONString(fanBaseInfoList);
            sendMessage(jsonString, username);
        }
        //开启订阅,将用户分组
        group.put(this.username, this.session);
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

        for (Session session : group.values()) {
            log.info("{}订阅fanscada数据", username);
            lock.lock();
            try {
                session.getAsyncRemote().sendText(message);
            } finally {
                lock.unlock();
            }
        }
    }

}