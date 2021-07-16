package com.zhikuntech.intellimonitor.messagepush.domain.service.impl;

import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.messagepush.domain.service.MessageService;
import com.zhikuntech.intellimonitor.messagepush.domain.websocket.MyWebSocketHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 滕楠
 * @className MessageServiceImpl
 * @create 2021/7/13 10:40
 **/
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private WebSocketServer webSocketServer;

    @Override
    public Boolean messagePush(String messageBody, Integer type) {
        if (type == 1) {
            return cableAlarmMessagePush(messageBody);
        } else if (type == 2) {
            return onlineAlarmMessagePush(messageBody);
        } else if (type == 3) {
            return structureAlarmMessagePush(messageBody);
        } else if (type == 4) {
            return alarmListMessagePush(messageBody);
        }
        return false;
    }

    private Boolean cableAlarmMessagePush(String messageBody) {
        webSocketServer.sendGroupMessage(messageBody, MyWebSocketHandler.cable.keySet());
        return true;
    }

    private Boolean onlineAlarmMessagePush(String messageBody) {
        return true;
    }

    private Boolean structureAlarmMessagePush(String messageBody) {
        return true;
    }

    private Boolean alarmListMessagePush(String messageBody) {
        return true;
    }


}