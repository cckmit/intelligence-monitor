package com.zhikuntech.intellimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author 代志豪
 * 2021/6/2 18:43
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.zhikuntech.intellimonitor.mainpage.domain.com.zhikuntech.intellimonitor.fanscada.domain.mapper")
public class MainPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainPageApplication.class, args);
    }
}
