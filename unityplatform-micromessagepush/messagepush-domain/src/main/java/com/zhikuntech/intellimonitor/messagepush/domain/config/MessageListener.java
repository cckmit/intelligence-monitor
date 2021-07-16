package com.zhikuntech.intellimonitor.messagepush.domain.config;

import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.messagepush.domain.constant.MessageConstant;
import com.zhikuntech.intellimonitor.messagepush.domain.websocket.MyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sun.util.locale.provider.LocaleServiceProviderPool;

import java.util.Optional;

/**
 * @author 滕楠
 * @className MessageService
 * @create 2021/7/8 11:38
 **/
@Component
@Slf4j
public class MessageListener {

    @Autowired
    WebSocketServer webSocketServer;

    @KafkaListener(topics = {"1"})
    public void cableAlarmMessagePush(ConsumerRecord<?, ?> record) {
        //卡夫卡拉取消息  topic
        Optional<? extends ConsumerRecord<?, ?>> result = Optional.ofNullable(record);
        if (result.isPresent()) {
            ConsumerRecord<?, ?> consumerRecord = result.get();
            Object value = consumerRecord.value();
            Object key = consumerRecord.key();
            log.info("topic{},partition{}",consumerRecord.topic(),consumerRecord.partition());
            webSocketServer.sendGroupMessage(value.toString(), MyWebSocketHandler.cable.keySet());

        }
    }

    @KafkaListener(topics = {"2"})
    public void onlineAlarmMessagePush(ConsumerRecord<?, ?> record) {
        //
        Optional<? extends ConsumerRecord<?, ?>> result = Optional.ofNullable(record);
        if (result.isPresent()) {
            Object value = record.value();
            Object key = record.key();
            webSocketServer.sendGroupMessage(value.toString(), MyWebSocketHandler.online.keySet());
        }
    }

    @KafkaListener(topics = {"3"})
    public void structureAlarmMessagePush(ConsumerRecord<?, ?> record) {
        //结构监测
    }

    @KafkaListener(topics = {"4"})
    public void alarmListMessagePush(ConsumerRecord<?, ?> record) {
        //分为十个标签页
    }
}