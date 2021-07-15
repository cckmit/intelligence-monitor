package com.zhikuntech.intellimonitor.onlinemonitor.domain.schedule;

import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.onlinemonitor.domain.websocket.GISWebsocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 杨锦程
 * @Date 2021/7/14 10:12
 * @Version 1.0
 */
@Component
@Slf4j
public class GISMonitor {
    @Scheduled(cron = "*/30 * * * * ?")
    public void monitor() {
        String runtimeLand = String.join(",", GISWebsocketHandler.GROUP_RUNTIME_LAND.keySet());
        String runtimeSea = String.join(",", GISWebsocketHandler.GROUP_RUNTIME_SEA.keySet());
        log.info("当前连接的websocket数量是{}，订阅陆上GIS实时数据的有{}，订阅海上GIS实时数据的有{}",
                WebSocketServer.clients.size(), runtimeLand, runtimeSea);

        ServerImplPool pool = GoldenUtil.pool;
        ConcurrentHashMap<String, ServerImpl> servers = GoldenUtil.servers;
        String serverKeys = String.join(",", servers.keySet());
        log.info("当前庚顿数据库连接池实际连接是{}，订阅庚顿数量是{}，名称为{}", pool.getRealSize(), servers.size(), serverKeys);
    }
}
