package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Component
@Slf4j
public class Monitor {

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private GoldenUtil goldenUtil;

    @Scheduled(cron = "*/30 * * * * ?")
    public void monitor(){
        ConcurrentHashMap<String, Session> clients = WebSocketServer.clients;
        String clientKeys = String.join(",", clients.keySet());
        log.info("当前连接的websocket数量是{}，名称为{}",clients.size(),clientKeys);

        ServerImplPool pool = goldenUtil.getPool();
        ConcurrentHashMap<String, ServerImpl> servers = goldenUtil.getServer();
        String serverKeys = String.join(",", servers.keySet());
        log.info("当前庚顿数据库连接池实际连接是{}，订阅庚顿数量是{}，名称为{}",pool.getRealSize(),servers.size(),serverKeys);
    }
}
