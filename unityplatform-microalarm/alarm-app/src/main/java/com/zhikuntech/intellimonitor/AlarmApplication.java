package com.zhikuntech.intellimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 告警服务
 *
 * @author liukai
 * @date 2021/07/02
 */
@MapperScan({"com.zhikuntech.intellimonitor.alarm.domain.mapper"})
@SpringBootApplication
public class AlarmApplication {

    public static void main(String[] args) {

        SpringApplication.run(AlarmApplication.class, args);
    }
}
