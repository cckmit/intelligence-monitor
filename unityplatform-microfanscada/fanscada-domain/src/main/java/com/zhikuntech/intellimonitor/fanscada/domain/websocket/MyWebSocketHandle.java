package com.zhikuntech.intellimonitor.fanscada.domain.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.BaseWebSocketHandler;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.SocketParam;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.LoopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 滕楠
 * @className MyWebSocketHandle
 * @create 2021/7/7 18:25
 **/
@Slf4j
@Component
public class MyWebSocketHandle implements BaseWebSocketHandler {

    //分组
    public static ConcurrentHashMap<String, Session> groupRuntime = new ConcurrentHashMap<>();

    @Autowired
    private FanIndexService fanIndexService;

    @Override
    public void onOpen(String username) {

    }

    @Override
    public void onClose(String username) {

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
            if (messageType.contains("01")) {
                //规定数据格式,解析以校验权限,分组,等.
                groupRuntime.put(username, session);
                log.info("接收到{}的消息,内容{}", username, messageType);
                List<LoopVO> fanBaseInfoList = fanIndexService.getFanBaseInfoList();
                String jsonString = JSONObject.toJSONString(fanBaseInfoList);
                sendMessage(jsonString, username);
                //开启订阅,将用户分组
            } else if (messageType.contains("02")) {
                log.info("重新订阅");
                groupRuntime.put(username, session);
                fanIndexService.getFanBaseInfoList(username);

            }
        } catch (JSONException e) {
            log.info(e.getMessage());
            sendMessage("消息格式错误,请重新发送", username);
        }
    }

    @Override
    public void onError(String username) {

    }
}