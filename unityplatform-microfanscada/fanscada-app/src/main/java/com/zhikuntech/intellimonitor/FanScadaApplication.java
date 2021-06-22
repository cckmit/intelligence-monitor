package com.zhikuntech.intellimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 滕楠
 * @className FanScadaApplication
 * @create 2021/6/10 18:08
 **/
@MapperScan({"com.zhikuntech.intellimonitor.fanscada.domain.mapper"})
@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
public class FanScadaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FanScadaApplication.class,args);
    }
}