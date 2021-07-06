package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.MyWebSocketHandler;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Component
@Slf4j
public class Monitor {

    @Scheduled(cron = "*/30 * * * * ?")
    public void monitor() {
        String runtime = String.join(",", MyWebSocketHandler.GROUP_RUNTIME.keySet());
        String statistics = String.join(",", MyWebSocketHandler.GROUP_STATISTICS.keySet());
        log.info("当前连接的websocket数量是{}，订阅风机数据的有{}，订阅统计数据的有{}",
                WebSocketServer.clients.size(), runtime, statistics);

        ServerImplPool pool = GoldenUtil.pool;
        ConcurrentHashMap<String, ServerImpl> servers = GoldenUtil.servers;
        String serverKeys = String.join(",", servers.keySet());
        log.info("当前庚顿数据库连接池实际连接是{}，订阅庚顿数量是{}，名称为{}", pool.getRealSize(), servers.size(), serverKeys);
    }
}
