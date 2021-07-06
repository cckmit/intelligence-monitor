package com.zhikuntech.intellimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 告警服务
 *
 * @author liukai
 * @date 2021/07/02
 */
@EnableEurekaClient
@MapperScan({"com.zhikuntech.intellimonitor.alarm.domain.mapper"})
@SpringBootApplication
public class AlarmApplication {

    public static void main(String[] args) {

        SpringApplication.run(AlarmApplication.class, args);
    }
}
