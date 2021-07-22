package com.zhikuntech.intellimonitor.fanscada.domain.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.BaseWebSocketHandler;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.fanscada.domain.constant.SocketConstant;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.SocketParam;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 滕楠
 * @className MyWebSocketHandle
 * @create 2021/7/7 18:25
 **/
@Slf4j
@Component
public class MyWebSocketHandle implements BaseWebSocketHandler {

    /**
     * 分组
     */
    public static ConcurrentHashMap<String, Session> groupRuntime = new ConcurrentHashMap<>();

    @Resource
    private FanIndexService fanIndexService;

    @Override
    public void onOpen(String username) {

    }

    @Override
    @OnClose
    public void onClose(String username) {
        groupRuntime.remove(username);
    }

    @Override
    public void onMessage(String message, String username) {
        try {
            SocketParam socketParam = JSON.parseObject(message, SocketParam.class);
            String messageType = socketParam.getMessageType();
            Session session = WebSocketServer.clients.get(username);
            if (null == session) {
                return;
            }
            groupRuntime.put(username, session);
            if (groupRuntime.size()>1){
                return;
            }
            if (messageType.contains(SocketConstant.MESSAGE_TYPE_01)) {
                //规定数据格式,解析以校验权限,分组,等.
                log.info("接收到{}的消息,内容{}", username, messageType);
                fanIndexService.getFanBaseInfoList(SocketConstant.GOLDEN_CONNECT_NAME);
                //开启订阅,将用户分组
            } else if (messageType.contains(SocketConstant.MESSAGE_TYPE_02)) {
                log.info("重新订阅");
                fanIndexService.getFanBaseInfoList(SocketConstant.GOLDEN_CONNECT_NAME);

            }
        } catch (JSONException e) {
            log.info(e.getMessage());
            sendMessage("消息格式错误,请重新发送", username);
        }
    }

    @Override
    public void onError(String username) {
        groupRuntime.remove(username);
    }
}