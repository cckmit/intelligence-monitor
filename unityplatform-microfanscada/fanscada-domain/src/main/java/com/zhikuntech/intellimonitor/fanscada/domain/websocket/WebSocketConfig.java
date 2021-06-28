package com.zhikuntech.intellimonitor.fanscada.domain.websocket;

import com.mysql.cj.x.protobuf.MysqlxExpr;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
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
    public void setGoldenUtil(GoldenUtil goldenUtil){
        WebSocketServer.goldenUtil = goldenUtil;
    }

    @Autowired
    public void setFanIndexService(FanIndexService fanIndexService){
        WebSocketServer.fanIndexService = fanIndexService;
    }
}