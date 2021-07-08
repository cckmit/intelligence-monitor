package com.zhikuntech.intellimonitor.core.commons.weabsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Configuration
@ConditionalOnProperty(prefix = "websocket" , havingValue = "true" ,value = "config")
public class WebSocketConfig {

    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setHandler(List<BaseWebSocketHandler> baseHandler) {
        WebSocketServer.baseHandlers = baseHandler;
    }
}