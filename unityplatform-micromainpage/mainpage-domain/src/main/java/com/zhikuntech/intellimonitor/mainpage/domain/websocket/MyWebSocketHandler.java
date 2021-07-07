package com.zhikuntech.intellimonitor.mainpage.domain.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.BaseWebSocketHandler;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketTransferVo;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 代志豪
 * 2021/7/6 14:32
 */
@Slf4j
@Component
public class MyWebSocketHandler implements BaseWebSocketHandler {

    @Autowired
    private FanInfoService fanInfoService;

    public static ConcurrentHashMap<String, Session> GROUP_STATISTICS = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Session> GROUP_RUNTIME = new ConcurrentHashMap<>();

    @Override
    public void onOpen(String username) {

    }

    @Override
    public void onClose(String username) {
        GROUP_RUNTIME.remove(username);
        GROUP_STATISTICS.remove(username);
    }

    @Override
    public void onMessage(String message, String username) {
        messageHandle(message, username);
    }

    @Override
    public void onError(String username) {
        GROUP_RUNTIME.remove(username);
        GROUP_STATISTICS.remove(username);
    }

    /**
     * 消息处理
     *
     * @param message 消息
     */
    private void messageHandle(String message, String username) {
        try {
            WebSocketTransferVo vo = JSON.parseObject(message, WebSocketTransferVo.class);
            Integer type = vo.getOrderType();
            String description = vo.getDescription();
            Set<String> strings = Arrays.stream(description.split(",")).collect(Collectors.toSet());
            if (type == 0) {
                //订阅
                if (WebSocketConstant.ALL.equals(description)) {
                    subscribeRuntime(username);
                    subscribeStatistics(username);
                } else {
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_RUNTIME)) {
                        subscribeRuntime(username);
                    }
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_STATISTICS)) {
                        subscribeStatistics(username);
                    }
                }
            } else if (type == 1) {
                //取消订阅
                if (WebSocketConstant.ALL.equals(description)) {
                    GROUP_RUNTIME.remove(username);
                    GROUP_STATISTICS.remove(username);
                } else {
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_RUNTIME)) {
                        GROUP_RUNTIME.remove(username);
                        log.info("取消订阅---风机详情");
                    }
                    if (strings.contains(WebSocketConstant.MAIN_PAGE_STATISTICS)) {
                        GROUP_STATISTICS.remove(username);
                        log.info("取消订阅---风场统计");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("转JSON失败");
            sendMessage("请发送正确的请求", username);
        }
    }

    /**
     * 订阅风场统计
     */
    private void subscribeStatistics(String username) {
        GROUP_STATISTICS.put(username, WebSocketServer.clients.get(username));
        log.info("用户{}，订阅风场统计", username);
        try {
            FanStatisticsDTO statistics = fanInfoService.getStatistics();
            String jsonString = JSONObject.toJSONString(statistics);
            jsonString = WebSocketConstant.MAIN_PAGE_STATISTICS + WebSocketConstant.PATTERN + jsonString;

            sendMessage(jsonString, username);
            fanInfoService.getStatistics("statistics");
            log.info("订阅golden实时消息---风场统计");
        } catch (Exception e) {
            GoldenUtil.cancelAll();
            sendAllMessage("重新订阅");
            e.printStackTrace();
            log.info("subscribeStatistics,websocket触发所有取消操作");
        }
    }

    /**
     * 订阅风机详情
     */
    private void subscribeRuntime(String username) {
        GROUP_RUNTIME.put(username, WebSocketServer.clients.get(username));
        log.info("用户{}，订阅风机详情", username);
        try {
            List<FanRuntimeDTO> runtimeInfos = fanInfoService.getRuntimeInfos();
            String jsonString = JSONObject.toJSONString(runtimeInfos);
            jsonString = WebSocketConstant.MAIN_PAGE_RUNTIME + WebSocketConstant.PATTERN + jsonString;
            sendMessage(jsonString, username);
            fanInfoService.getRuntimeInfos("runtime");
            log.info("订阅golden实时消息---风机详情");
        } catch (Exception e) {
            GoldenUtil.cancelAll();
            sendAllMessage("重新订阅");
            e.printStackTrace();
            log.info("subscribeRuntime,websocket触发所有取消操作");
        }
    }
}
