package com.zhikuntech.intellimonitor.onlinemonitor.domain.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.BaseWebSocketHandler;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketTransferVo;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.service.TransformerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 代志豪
 * 2021/7/9 10:36
 */
@Component
@Slf4j
public class TransformerWebsocketHandler implements BaseWebSocketHandler {

    @Autowired
    private TransformerService transformerService;

    public static ConcurrentHashMap<String, Session> GROUP_RUNTIME_LAND = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Session> GROUP_RUNTIME_SEA = new ConcurrentHashMap<>();
    public static final String GOLDEN_USER_LAND = "online_runtime_land";
    public static final String GOLDEN_USER_SEA = "online_runtime_sea";

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
            String runtimeSea = WebSocketConstant.ONLINE_MONITOR_RUNTIME_SEA;
            String runtimeLand = WebSocketConstant.ONLINE_MONITOR_RUNTIME_LAND;
            String graphSea = WebSocketConstant.ONLINE_MONITOR_GRAPH_SEA;
            String graphLand = WebSocketConstant.ONLINE_MONITOR_GRAPH_LAND;
            if (type == 0) {
                //订阅
                if (strings.contains(runtimeSea)) {
                    subscribeRuntime(username, 2, runtimeSea);
                }
                if (strings.contains(runtimeLand)) {
                    subscribeRuntime(username, 1, runtimeLand);
                }
                if (strings.contains(graphSea)) {
                    transformerService.getCache(username, 2);
                    subscribeRuntime(username, 2, graphSea);
                }
                if (strings.contains(graphLand)) {
                    transformerService.getCache(username, 1);
                    subscribeRuntime(username, 1, graphLand);
                }
            } else if (type == 1) {
                //取消订阅
                if (strings.contains(runtimeLand)||strings.contains(graphLand)) {
                    GROUP_RUNTIME_LAND.remove(username);
                    log.info("取消订阅---陆上变压器实时数据");
                }
                if (strings.contains(runtimeSea) || strings.contains(graphSea)) {
                    GROUP_RUNTIME_SEA.remove(username);
                    log.info("取消订阅---海上变压器实时数据");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("转JSON失败");
            sendMessage("请发送正确的请求", username);
        }
    }

    private void subscribeRuntime(String username, Integer num, String websocketDescription) {
        String goldenUser;
        if (num == 1) {
            GROUP_RUNTIME_LAND.put(username, WebSocketServer.clients.get(username));
            goldenUser = GOLDEN_USER_LAND;
        } else if (num == 2) {
            GROUP_RUNTIME_SEA.put(username, WebSocketServer.clients.get(username));
            goldenUser = GOLDEN_USER_SEA;
        } else {
            return;
        }
        log.info("用户{}，订阅变压器实时数据", username);
        try {
            transformerService.getTransformerRuntime(goldenUser, num, websocketDescription);
            log.info("订阅golden实时消息---变压器实时数据");
        } catch (Exception e) {
            GoldenUtil.cancelAll();
            sendAllMessage("重新订阅");
            e.printStackTrace();
            log.info("subscribeRuntime,websocket触发所有取消操作");
        }
    }
}
