package com.zhikuntech.intellimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author
 * 2021/7/15 16:43
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.zhikuntech.intellimonitor.cable.domain.mapper")
@EnableScheduling
@EnableFeignClients
public class CableApplication {
    public static void main(String[] args) {
        SpringApplication.run(CableApplication.class, args);
    }
}
