package com.zhikuntech.intellimonitor.integratedautomation.domain.schedule;

import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.integratedautomation.domain.service.impl.IntegratedAutomationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author 代志豪
 * 2021/6/17 17:04
 */
@Component
@Slf4j
public class IntegratedAutomationInit implements CommandLineRunner {

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

    @Autowired
    private IntegratedAutomationServiceImpl service;

    @Override
    public void run(String... args) {
        GoldenUtil.init(ip, port, user, password, poolSize, maxSize);
        int[] land = new int[]{};
        int[] sea = new int[]{};
        int[] controlCenter = new int[]{};
        try {
            service.subscribe("land", land);
            service.subscribe("sea", sea);
            service.subscribe("controlCenter", controlCenter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("初始化数据完成！");
    }
}
