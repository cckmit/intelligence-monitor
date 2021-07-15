package com.zhikuntech.intellimonitor.structuremonitor.domain.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 滕楠
 * @className Listener
 * @create 2021/7/14 10:42
 **/
@Component
@Slf4j
public class Listener {

    private Integer time = 1;

    @Scheduled(cron = "0 */1 * * * *?")
    public void getDataFromSqlServer() {
        log.info("执行次数{}", time++);

    }
}