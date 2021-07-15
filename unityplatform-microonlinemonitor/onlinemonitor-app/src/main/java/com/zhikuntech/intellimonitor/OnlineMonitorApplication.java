package com.zhikuntech.intellimonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 代志豪
 * 2021/7/9 10:01
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
public class OnlineMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineMonitorApplication.class, args);
    }
}
