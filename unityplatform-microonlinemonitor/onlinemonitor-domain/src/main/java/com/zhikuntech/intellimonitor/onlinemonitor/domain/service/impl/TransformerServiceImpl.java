package com.zhikuntech.intellimonitor.onlinemonitor.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.TimerUtil;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.dto.TransformerRuntimeDTO;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.schedule.OnlineMonitorInit;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.service.TransformerService;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.websocket.TransformerWebsocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * @author 代志豪
 * 2021/7/9 11:18
 */
@Slf4j
@Service
public class TransformerServiceImpl implements TransformerService {

    @Autowired
    private TransformerWebsocketHandler websocketHandler;

    @Override
    public void getTransformerRuntime(String user, Integer num) throws Exception {
        Set<String> strings;
        String s = "";
        if (num == 1) {
            strings = TransformerWebsocketHandler.GROUP_RUNTIME_LAND.keySet();
            s = WebSocketConstant.ONLINE_MONITOR_RUNTIME_LAND + WebSocketConstant.PATTERN;
        } else if (num == 2) {
            strings = TransformerWebsocketHandler.GROUP_RUNTIME_SEA.keySet();
            s = WebSocketConstant.ONLINE_MONITOR_RUNTIME_SEA + WebSocketConstant.PATTERN;
        } else {
            return;
        }
        if (GoldenUtil.servers.containsKey(user)) {
            startTimer(user, num, strings);
            return;
        }
        List<TransformerRuntimeDTO> list = new ArrayList<>();
        for (int i = 1; i < num + 1; i++) {
            TransformerRuntimeDTO dto = new TransformerRuntimeDTO();
            dto.setNum(i);
            list.add(dto);
        }
        if (strings.size() > 0) {
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 179, 192};
            try {
                String finalString = s;
                GoldenUtil.subscribeSnapshots(user, ids, (data) -> {
                    try {
                        if (strings.size() > 0) {
                            long l0 = System.currentTimeMillis();
//                            DataConvertUtils.convertAndSend(data);
//                            long l01 = System.currentTimeMillis();
//                            log.info("发送kafka耗时{}",l01-l0);
                            List<TransformerRuntimeDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data, (key) -> OnlineMonitorInit.GOLDEN_ID_MAP.get(key));
                            if (null != dtos) {
                                Map<String, Object> map = new HashMap<>(2);
                                map.put("time", data[0].getDate().getTime() / 1000);
                                map.put("items", dtos);
                                String jsonString = JSONObject.toJSONString(map, WriteMapNullValue);
                                jsonString = finalString + jsonString;
                                try {
                                    websocketHandler.sendGroupMessage(jsonString, strings);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            startTimer(user, num, strings);
                            long l1 = System.currentTimeMillis();
                            log.info("实时数据，处理时间{}，消息时间{}", l1 - l0, data[0].getDate());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != TimerUtil.TIMER_MAP.get(user)) {
                            TimerUtil.stop(user);
                        }
                        GoldenUtil.cancel(user);
                        websocketHandler.sendGroupMessage("重新订阅", strings);
                        log.info("回调函数内部触发取消操作");
                    }
                });
            } catch (SocketException e) {
                log.info("golden连接失败，重连后取消之前所有连接");
                GoldenUtil.servers.clear();
                e.printStackTrace();
                websocketHandler.sendAllMessage("重新订阅");
                log.info("变压器实时数据—_—触发所有取消操作");
            }
        }
    }


    /**
     * 开启定时任务
     */
    private void startTimer(String user, Integer num, Set<String> strings) {
        TimerUtil.start(new TimerTask() {
            @Override
            public void run() {
                GoldenUtil.servers.remove(user);
                if ("runtime".equals(user)) {
                    try {
                        getTransformerRuntime(user, num);
                    } catch (Exception e) {
                        e.printStackTrace();
                        websocketHandler.sendGroupMessage("重新订阅", strings);
                    }
                }
                log.info("定时任务取消golden连接,{}", user);
            }
        }, user);
    }
}
