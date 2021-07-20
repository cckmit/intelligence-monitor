package com.zhikuntech.intellimonitor.messagepush.domain.websocket;

import com.zhikuntech.intellimonitor.core.commons.weabsocket.BaseWebSocketHandler;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.messagepush.domain.constant.MessageConstant;
import com.zhikuntech.intellimonitor.messagepush.domain.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 滕楠
 * @className MyWebSocketHandler
 * @create 2021/7/8 10:47
 **/
@Component
@Slf4j
public class MyWebSocketHandler implements BaseWebSocketHandler {
    /**
     * 分组 海缆,在线,结构,告警列表
     */
    public static ConcurrentHashMap<String, Session> cable = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Session> online = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Session> structure = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Session> alarm = new ConcurrentHashMap<>();

    @Override
    @OnOpen
    public void onOpen(String username) {

    }

    @Override
    @OnClose
    public void onClose(String username) {
        log.info("{}关闭,从分组移除",username);
        String moduleName = username.split("_")[1];
        if (MessageConstant.CABLE_MODULE.equals(moduleName)) {
            cable.remove(username);
        } else if (MessageConstant.ONLINE_MODULE.equals(moduleName)) {
            online.remove(username);
        } else if (MessageConstant.STRUCTURE_MODULE.equals(moduleName)) {
            structure.remove(username);
        } else if (MessageConstant.ALARM_MODULE.equals(moduleName)) {
            alarm.remove(username);
        }
    }

    @Override
    @OnMessage
    public void onMessage(String message, String username) {
        String moduleName = username.split("_")[1];
        Session session = WebSocketServer.clients.get(username);
        if (MessageConstant.CABLE_MODULE.equals(moduleName)) {
            cable.put(username,session);
            if (cable.size()>1){
                return;
            }
        } else if (MessageConstant.ONLINE_MODULE.equals(moduleName)) {
            online.put(username,session);
            if (online.size()>1){
                return;
            }
        } else if (MessageConstant.STRUCTURE_MODULE.equals(moduleName)) {
            structure.put(username,session);
            if (structure.size()>1){
                return;
            }
        } else if (MessageConstant.ALARM_MODULE.equals(moduleName)) {
            alarm.put(username,session);
            if (alarm.size()>1){
                return;
            }
        }

    }

    @Override
    @OnError
    public void onError(String username) {

    }

}