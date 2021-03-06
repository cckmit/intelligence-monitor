package com.zhikuntech.intellimonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 滕楠
 * @className StructureMonitorApplication
 * @create 2021/7/6 15:03
 **/
@SpringBootApplication
@EnableScheduling
public class StructureMonitorApplication {
    public static void main(String[] args) {

        SpringApplication.run(StructureMonitorApplication.class,args);
    }
}