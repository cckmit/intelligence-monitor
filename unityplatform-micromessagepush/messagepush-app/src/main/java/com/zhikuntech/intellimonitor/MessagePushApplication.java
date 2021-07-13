package com.zhikuntech.intellimonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 滕楠
 * @className MessagePushApplication
 * @create 2021/7/6 11:43
 **/
@SpringBootApplication
//@EnableEurekaClient
//@EnableScheduling
//@EnableFeignClients
public class MessagePushApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessagePushApplication.class,args);
    }
}