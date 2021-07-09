package com.zhikuntech.intellimonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 代志豪
 * 2021/7/9 10:01
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class OnlineMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineMonitorApplication.class, args);
    }
}
