package com.zhikuntech.intellimonitor.onlinemonitor.domain.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.BaseWebSocketHandler;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketTransferVo;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.service.StationGISService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author 杨锦程
 * @Date 2021/7/9 14:42
 * @Description GIS websocket处理
 * @Version 1.0
 */
@Slf4j
@Component
public class GISWebsocketHandler implements BaseWebSocketHandler {
    @Autowired
    private StationGISService stationGISService;

    public static ConcurrentHashMap<String, Session> GROUP_RUNTIME_LAND = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Session> GROUP_RUNTIME_SEA = new ConcurrentHashMap<>();

    @Override
    public void onOpen(String username) {

    }

    @Override
    public void onClose(String username) {
        GROUP_RUNTIME_LAND.remove(username);
        GROUP_RUNTIME_SEA.remove(username);
    }

    @Override
    public void onMessage(String message, String username) {
        log.info(message);
        messageHandle(message, username);
    }

    @Override
    public void onError(String username) {
        GROUP_RUNTIME_LAND.remove(username);
        GROUP_RUNTIME_SEA.remove(username);
    }

    private void messageHandle(String message, String username) {
        try {
            WebSocketTransferVo vo = JSON.parseObject(message, WebSocketTransferVo.class);
            Integer type = vo.getOrderType();
            String description = vo.getDescription();
            Set<String> strings = Arrays.stream(description.split(",")).collect(Collectors.toSet());
            if (type == 0) {
                //订阅
                //海上32个采集点
                if (strings.contains(WebSocketConstant.ONLINE_MONITOR_GIS_RUNTIME_SEA)) {
                    subscribeRuntime(username, 32);
                }
                //陆上8个采集点
                if (strings.contains(WebSocketConstant.ONLINE_MONITOR_GIS_RUNTIME_LAND)) {
                    subscribeRuntime(username, 8);
                }
            } else if (type == 1) {
                //取消订阅
                if (WebSocketConstant.ALL.equals(description)) {
                    GROUP_RUNTIME_LAND.remove(username);
                    GROUP_RUNTIME_SEA.remove(username);
                } else {
                    if (strings.contains(WebSocketConstant.ONLINE_MONITOR_GIS_RUNTIME_LAND)) {
                        GROUP_RUNTIME_LAND.remove(username);
                        log.info("取消订阅---陆上GIS实时数据");
                    } else if (strings.contains(WebSocketConstant.ONLINE_MONITOR_GIS_RUNTIME_SEA)) {
                        GROUP_RUNTIME_SEA.remove(username);
                        log.info("取消订阅---海上GIS实时数据");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("转JSON失败");
            sendMessage("请发送正确的请求", username);
        }
    }

    private void subscribeRuntime(String username, Integer num) {
        String goldenUser = "";
        if (num == 8) {
            GROUP_RUNTIME_LAND.put(username, WebSocketServer.clients.get(username));
            goldenUser = "online_gis_runtime_land";
        } else if (num == 32) {
            GROUP_RUNTIME_SEA.put(username, WebSocketServer.clients.get(username));
            goldenUser = "online_gis_runtime_sea";
        } else {
            return;
        }
        log.info("用户{}，订阅GIS实时数据", username);
        try {
            stationGISService.getGISRuntime(goldenUser, num);
            log.info("订阅golden实时消息---GIS实时数据");
        } catch (Exception e) {
            GoldenUtil.cancelAll();
            sendAllMessage("重新订阅");
            e.printStackTrace();
            log.info("subscribeRuntime,websocket触发所有取消操作");
        }
    }
}
