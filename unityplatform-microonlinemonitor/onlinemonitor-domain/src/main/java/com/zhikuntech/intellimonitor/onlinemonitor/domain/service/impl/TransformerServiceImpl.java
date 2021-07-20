package com.zhikuntech.intellimonitor.onlinemonitor.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.TimerUtil;
import com.zhikuntech.intellimonitor.core.stream.DataConvertUtils;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.dto.TransformerRuntimeDTO;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.schedule.OnlineMonitorInit;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.service.TransformerService;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.utils.BeanConvertUtils;
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

    private final static int CAPACITY = 1800;

    public static List<TransformerRuntimeDTO> list_land = new ArrayList<>();
    public static List<TransformerRuntimeDTO> list_sea_1 = new ArrayList<>();
    public static List<TransformerRuntimeDTO> list_sea_2 = new ArrayList<>();

    @Override
    public void getTransformerRuntime(String user, Integer num, String websocketDescription) throws Exception {
        Set<String> strings;
        String string = websocketDescription + WebSocketConstant.PATTERN;
        if (num == 1) {
            strings = TransformerWebsocketHandler.GROUP_RUNTIME_LAND.keySet();
        } else if (num == 2) {
            strings = TransformerWebsocketHandler.GROUP_RUNTIME_SEA.keySet();
        } else {
            return;
        }
        if (GoldenUtil.servers.containsKey(user)) {
            startTimer(user, num, websocketDescription, strings);
            return;
        }
        List<TransformerRuntimeDTO> list = new ArrayList<>();
        for (int i = 1; i < num + 1; i++) {
            TransformerRuntimeDTO dto = new TransformerRuntimeDTO();
            dto.setNum(i);
            list.add(dto);
        }
        if (strings.size() > 0) {
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 179, 192, 1196};
            try {
                GoldenUtil.subscribeSnapshots(user, ids, (data) -> {
                    try {
                        if (GoldenUtil.servers.containsKey(user)) {
                            long l0 = System.currentTimeMillis();
                            //将数据推送kafka
//                            DataConvertUtils.convertAndSend(data);
//                            long l01 = System.currentTimeMillis();
//                            log.info("发送kafka耗时{}",l01-l0);
                            List<TransformerRuntimeDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data, (key) -> OnlineMonitorInit.GOLDEN_ID_MAP.get(key));
                            log.info(data[0].getDate().getTime() + "");
                            if (null != dtos) {
                                dtos.forEach(e -> e.setTimeStamp((data[0].getDate().getTime() / 1000) * 1000));
                                //缓存数据，用于前端页面曲线图展示
                                fillList(dtos);
                                if (strings.size() > 0) {
                                    startTimer(user, num, websocketDescription, strings);
                                    String jsonString = JSONObject.toJSONString(dtos, WriteMapNullValue);
                                    jsonString = string + jsonString;
                                    try {
                                        websocketHandler.sendGroupMessage(jsonString, strings);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            long l1 = System.currentTimeMillis();
                            log.info("实时数据{}，处理时间{}，消息时间{}", user, l1 - l0, data[0].getDate());
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

    private void fillList(List<TransformerRuntimeDTO> dtos) {
        if (list_sea_1.size() == CAPACITY) {
            list_sea_1.remove(0);
        }
        if (list_sea_2.size() == CAPACITY) {
            list_sea_2.remove(0);
        }
        if (list_land.size() == CAPACITY) {
            list_land.remove(0);
        }
        Class<TransformerRuntimeDTO> targetClass = TransformerRuntimeDTO.class;
        if (dtos.size() > 1) {
            list_sea_1.add(BeanConvertUtils.copyProperties(dtos.get(0), targetClass));
            list_sea_2.add(BeanConvertUtils.copyProperties(dtos.get(1), targetClass));
        } else {
            list_land.add(BeanConvertUtils.copyProperties(dtos.get(0), targetClass));
        }
    }

    @Override
    public void getCache(String username, Integer num) {
        String jsonString;
        String s;
        List<List<TransformerRuntimeDTO>> lists = new ArrayList<>();
        if (num == 1) {
            lists.add(list_land);
            s = WebSocketConstant.ONLINE_MONITOR_GRAPH_LAND + WebSocketConstant.PATTERN;
        } else {
            lists.add(list_sea_1);
            lists.add(list_sea_2);
            s = WebSocketConstant.ONLINE_MONITOR_GRAPH_SEA + WebSocketConstant.PATTERN;
        }
        jsonString = JSONObject.toJSONString(lists, WriteMapNullValue);
        websocketHandler.sendMessage(s + jsonString, username);
    }

    /**
     * 开启定时任务
     */
    private void startTimer(String user, Integer num, String websocketDescription, Set<String> strings) {
        TimerUtil.start(new TimerTask() {
            @Override
            public void run() {
                GoldenUtil.cancel(user);
                try {
                    getTransformerRuntime(user, num, websocketDescription);
                } catch (Exception e) {
                    websocketHandler.sendGroupMessage("重新订阅", strings);
                    e.printStackTrace();
                    log.info("定时任务取消golden连接,{}", user);
                }
            }
        }, user);
    }
}
