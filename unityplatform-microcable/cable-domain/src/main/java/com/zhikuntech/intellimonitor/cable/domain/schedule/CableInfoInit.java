package com.zhikuntech.intellimonitor.cable.domain.schedule;

import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CableInfoInit implements CommandLineRunner {
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

    public static Map<String, Integer> GOLDEN_ID_MAP = new HashMap<>();

    public static Map<String, BigDecimal> POWER_MAP = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        GoldenUtil.init(ip, port, user, password, poolSize, maxSize);
        log.info("初始化数据完成！");
    }
}
