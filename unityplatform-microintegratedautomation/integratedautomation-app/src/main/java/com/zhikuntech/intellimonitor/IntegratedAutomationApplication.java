package com.zhikuntech.intellimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author 代志豪
 * 2021/7/20 9:03
 */
@MapperScan("com.zhikuntech.intellimonitor.integratedautomation.domain.mapper")
@SpringBootApplication
@EnableEurekaClient
public class IntegratedAutomationApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegratedAutomationApplication.class, args);
    }
}
