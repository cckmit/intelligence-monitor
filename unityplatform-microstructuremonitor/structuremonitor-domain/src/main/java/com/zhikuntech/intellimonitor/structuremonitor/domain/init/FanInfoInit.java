package com.zhikuntech.intellimonitor.structuremonitor.domain.init;

import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
@Order(1)
public class FanInfoInit implements CommandLineRunner {

    @Value("${golden.ip}")
    private String ip;

    @Value("${golden.port}")
    private Integer port;

    @Value("${golden.user}")
    private String user;

    @Value("${golden.password}")
    private String password;

    @Value("${golden.poolSize}")
    private Integer poolSize;

    @Value("${golden.maxSize}")
    private Integer maxSize;

    @Override
    public void run(String... args) {
        GoldenUtil.init(ip, port, user, password, poolSize, maxSize);
        log.info("初始化数据完成！");
    }

}
