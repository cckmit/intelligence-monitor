package com.zhikuntech.intellimonitor.onlinemonitor.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.TimerUtil;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.dto.StationGISDTO;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.schedule.OnlineMonitorInit;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.service.StationGISService;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.websocket.GISWebsocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * @Author 杨锦程
 * @Date 2021/7/12 17:18
 * @Description GIS接口实现类
 * @Version 1.0
 */
@Service
@Slf4j
public class StationGISServiceImpl implements StationGISService {

    @Autowired
    private GISWebsocketHandler gisWebsocketHandler;

    @Override
    public void getGISRuntime(String user, Integer num) throws Exception {
        Set<String> strings;
        String s = "";
        if (num == 8) {
            strings = GISWebsocketHandler.GROUP_RUNTIME_LAND.keySet();
            s = WebSocketConstant.ONLINE_MONITOR_GIS_RUNTIME_LAND + WebSocketConstant.PATTERN;
        } else if (num == 32) {
            strings = GISWebsocketHandler.GROUP_RUNTIME_SEA.keySet();
            s = WebSocketConstant.ONLINE_MONITOR_GIS_RUNTIME_SEA + WebSocketConstant.PATTERN;
        } else {
            return;
        }
        if (GoldenUtil.servers.containsKey(user)) {
            startTimer(user, num, strings);
            return;
        }
        List<StationGISDTO> list = new ArrayList<>();
        for (int i = 1; i < num + 1; i++) {
            StationGISDTO dto = new StationGISDTO();
            //添加采集点序号
            dto.setPatchPoint(i);
            list.add(dto);
        }
        if (strings.size() > 0) {
            int[] ids = new int[]{135,136};
            try {
                String finalString = s;
                GoldenUtil.subscribeSnapshots(user, ids, (data) -> {
                    try {
                        if (strings.size() > 0) {
                            long l0 = System.currentTimeMillis();
                            List<StationGISDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data, (key) -> OnlineMonitorInit.GOLDEN_ID_MAP.get(key));
                            if (null != dtos) {
                                Map<String, Object> map = new HashMap<>(2);
                                map.put("time", data[0].getDate().getTime() / 1000);
                                map.put("items", dtos);
                                String jsonString = JSONObject.toJSONString(map, WriteMapNullValue);
                                jsonString = finalString + jsonString;
                                try {
                                    gisWebsocketHandler.sendGroupMessage(jsonString, strings);
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
                        gisWebsocketHandler.sendGroupMessage("重新订阅", strings);
                        log.info("回调函数内部触发取消操作");
                    }
                });
            } catch (SocketException e) {
                log.info("golden连接失败，重连后取消之前所有连接");
                GoldenUtil.servers.clear();
                e.printStackTrace();
                gisWebsocketHandler.sendAllMessage("重新订阅");
                log.info("GIS实时数据—_—触发所有取消操作");
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
                        getGISRuntime(user, num);
                    } catch (Exception e) {
                        e.printStackTrace();
                        gisWebsocketHandler.sendGroupMessage("重新订阅", strings);
                    }
                }
                log.info("定时任务取消golden连接,{}", user);
            }
        }, user);
    }

}
