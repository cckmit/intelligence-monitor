package com.zhikuntech.intellimonitor.mainpage.domain.websocket;

import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Configuration
public class WebSocketConfig {

    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setGoldenUtil(GoldenUtil GoldenUtil) {
        WebSocketServer.GoldenUtil = GoldenUtil;
    }

    @Autowired
    public void setFanInfoService(FanInfoService fanInfoService) {
        WebSocketServer.fanInfoService = fanInfoService;
    }
}