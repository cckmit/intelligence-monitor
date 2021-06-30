package com.zhikuntech.intellimonitor.mainpage.domain.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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

    public static ConcurrentHashMap<String, Session> GROUP_STATISTICS = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Session> GROUP_RUNTIME = new ConcurrentHashMap<>();

    private Session session;

    private String username;

    private ReentrantLock lock = new ReentrantLock();

    public static GoldenUtil goldenUtil;

    public static FanInfoService fanInfoService;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        onlineCount.incrementAndGet();
        this.session = session;
        this.username = username;
        clients.put(username, session);
        log.info("有新连接加入：{}，当前在线人数为：{}", this.username, onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        onlineCount.decrementAndGet();
        clients.remove(username);
        GROUP_STATISTICS.remove(username);
        GROUP_RUNTIME.remove(username);
        log.info("有一连接关闭：{}，当前在线人数为：{}", username, onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        if ("hello".equals(message)) {
            log.info(message);
            sendMessage("world", username);
            log.info("world");
        } else {
            messageHandle(message);
        }
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
            e.printStackTrace();
            log.error("sendMessage服务端发送消息给客户端失败：", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 分组推送消息
     *
     * @param message 推送消息内容
     * @param type    group分组 0：GROUP_RUNTIME，1：GROUP_STATISTICS
     */
    public void sendGroupMessage(String message, Integer type) {
        lock.lock();
        try {
            if (type == 0) {
                for (Session session : GROUP_RUNTIME.values()) {
                    session.getBasicRemote().sendText(message);
                }
            } else if (type == 1) {
                for (Session session : GROUP_STATISTICS.values()) {
                    session.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("sendGroupMessage服务端发送消息给客户端失败：", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 群发消息(所有人)
     *
     * @param message 消息内容
     */
    public void sendAllMessage(String message) {
        lock.lock();
        try {
            for (Session session : clients.values()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("sendAllMessage服务端发送消息给客户端失败：", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消息处理
     *
     * @param message 消息
     */
    private void messageHandle(String message) {
        try {
            WebSocketTransferVo vo = JSON.parseObject(message, WebSocketTransferVo.class);
            Integer type = vo.getOrderType();
            String description = vo.getDescription();
            Set<String> strings = Arrays.stream(description.split(",")).collect(Collectors.toSet());
            if (type == 0) {
                //订阅
                if (WebSocketConstant.ALL.equals(description)) {
                    subscribeRuntime();
                    subscribeStatistics();
                } else {
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_RUNTIME)) {
                        subscribeRuntime();
                    }
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_STATISTICS)) {
                        subscribeStatistics();
                    }
                }
            } else if (type == 1) {
                //取消订阅
                if (WebSocketConstant.ALL.equals(description)) {
                    GROUP_RUNTIME.remove(username);
                    GROUP_STATISTICS.remove(username);
                } else {
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_RUNTIME)) {
                        GROUP_RUNTIME.remove(username);
                        log.info("取消订阅---风机详情");
                    }
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_STATISTICS)) {
                        GROUP_STATISTICS.remove(username);
                        log.info("取消订阅---风场统计");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("转JSON失败");
            sendMessage("请发送正确的请求", username);
        }
    }

    /**
     * 订阅风场统计
     */
    private void subscribeStatistics() {
        GROUP_STATISTICS.put(username, session);
        log.info("用户{}，订阅风场统计", username);
        try {
            FanStatisticsDTO statistics = fanInfoService.getStatistics();
            String jsonString = JSONObject.toJSONString(statistics);
            jsonString = WebSocketConstant.MAIN_PAGE_STATISTICS + WebSocketConstant.PATTERN + jsonString;
            sendMessage(jsonString, username);
            fanInfoService.getStatistics("statistics");
            log.info("订阅golden实时消息---风场统计");
        } catch (Exception e) {
            goldenUtil.cancelAll();
            sendAllMessage("重新订阅");
            e.printStackTrace();
            log.info("subscribeStatistics,websocket触发所有取消操作");
        }
    }

    /**
     * 订阅风机详情
     */
    private void subscribeRuntime() {
        GROUP_RUNTIME.put(username, session);
        log.info("用户{}，订阅风机详情", username);
        try {
            List<FanRuntimeDTO> runtimeInfos = fanInfoService.getRuntimeInfos();
            String jsonString = JSONObject.toJSONString(runtimeInfos);
            jsonString = WebSocketConstant.MAIN_PAGE_RUNTIME + WebSocketConstant.PATTERN + jsonString;
            sendMessage(jsonString, username);
            fanInfoService.getRuntimeInfos("runtime");
            log.info("订阅golden实时消息---风机详情");
        } catch (Exception e) {
            goldenUtil.cancelAll();
            sendAllMessage("重新订阅");
            e.printStackTrace();
            log.info("subscribeRuntime,websocket触发所有取消操作");
        }
    }

//    private void aaa(String message) {
//        if ("reset".equals(message)) {
//            goldenUtil.cancelAll();
//            sendAllMessage("重新订阅");
//            log.info("手动触发所有取消操作");
//        }
//        if ("reset1".equals(message)) {
//            GROUP_RUNTIME.remove(username);
//            log.info("取消订阅---风机详情");
//        }
//        if ("reset2".equals(message)) {
//            GROUP_STATISTICS.remove(username);
//            log.info("取消订阅---风场统计");
//        }
//        Set<String> strings = Arrays.stream(message.split(",")).collect(Collectors.toSet());
//        if (strings.contains("1")) {
//            subscribeRuntime();
//        }
//        if (strings.contains("2")) {
//            subscribeStatistics();
//        }
//    }
}