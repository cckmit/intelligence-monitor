package com.zhikuntech.intellimonitor.fanscada.domain.config;

import com.rtdb.api.model.ValueData;
import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.ServerImplPool;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.core.commons.weabsocket.WebSocketServer;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 代志豪
 * @date 2021-06-07
 */
@Component
@Slf4j
public class Monitor {

    @Autowired
    private GoldenUtil goldenUtil;

    @Autowired
    private BackendToGoldenService backendToGoldenService;

    @Scheduled(cron = "*/30 * * * * ?")
    public void monitor() {
        ConcurrentHashMap<String, Session> clients = WebSocketServer.clients;
        String clientKeys = String.join(",", clients.keySet());
        log.info("当前连接的websocket数量是{}，名称为{}", clients.size(), clientKeys);

        ServerImplPool pool = goldenUtil.getPool();
        ConcurrentHashMap<String, ServerImpl> servers = goldenUtil.getServer();
        String serverKeys = String.join(",", servers.keySet());
        log.info("当前庚顿数据库连接池实际连接是{}，订阅庚顿数量是{}，名称为{}", pool.getRealSize(), servers.size(), serverKeys);
    }

    // 检测是否有socket在订阅,没有的话取消golden连接
    //private static Integer size;

    //@Scheduled(cron = "*/5 * * * * ?")
    //public void goldenCheck() {
    //    ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
    //    ConcurrentHashMap<String, ServerImpl> servers = goldenUtil.getServer();
    //    //golden订阅连接==0的时候重新建立连接
    //    if (servers.size() == 0) {
    //        GoldenUtil bean = applicationContext.getBean(GoldenUtil.class);
    //        bean.cancelAll();
    //        FanIndexService fanIndexService = applicationContext.getBean(FanIndexService.class);
    //        fanIndexService.getFanBaseInfoList("root");
    //    }
    //}


    /**
     * 每日0:00执行 获取当日零点发电量
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void init() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(192);
        List<BackendToGolden> backendToGoldens = backendToGoldenService.selectList(list);
        int[] ints = backendToGoldens.stream().mapToInt(BackendToGolden::getGoldenId).toArray();
        List<ValueData> snapshots = goldenUtil.getSnapshots(ints);

        for (BackendToGolden backendToGolden : backendToGoldens) {
            for (ValueData snapshot : snapshots) {
                if (backendToGolden.getGoldenId() == snapshot.getId()) {
                    StartUpInitForPow.initMap.put(FanConstant.DAILY_POWER + backendToGolden.getNumber(), snapshot.getValue());
                }
            }
        }
        log.info("每日发电量更新完成");
    }
}
