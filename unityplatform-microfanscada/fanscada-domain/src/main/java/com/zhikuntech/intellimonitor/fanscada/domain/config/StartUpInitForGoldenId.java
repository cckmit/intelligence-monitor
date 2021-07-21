package com.zhikuntech.intellimonitor.fanscada.domain.config;

import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 滕楠
 * @className DataInitConf
 * @create 2021/6/21 15:40
 **/
@Component
@Slf4j
@Order(value = 1)
public class StartUpInitForGoldenId implements CommandLineRunner {

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

    public static Map<String, Integer> initMap = new HashMap<>();

    @Autowired
    private BackendToGoldenService backendToGoldenService;

    @Override
    public void run(String... args) throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(12);
        list.add(13);
        list.add(179);
        List<BackendToGolden> result = backendToGoldenService.selectList(list);
        for (int i = 1; i < 64; i++) {
            for (BackendToGolden backendToGolden : result) {
                initMap.put(FanConstant.GOLDEN_ID + backendToGolden.getBackendId() + "_" + i, backendToGolden.getGoldenId());
            }
        }

        List<BackendToGolden> result2 = backendToGoldenService.getListByBackendId(192);
        for (int i = 1; i < 64; i++) {
            for (BackendToGolden backendToGolden : result2) {
                initMap.put(FanConstant.DAILY_POWER_ALL + backendToGolden.getBackendId() + "_" + i, backendToGolden.getGoldenId());
            }
        }
        GoldenUtil.init(ip, port, user, password, poolSize, maxSize);
        log.info("初始化数据完成");
    }

}
