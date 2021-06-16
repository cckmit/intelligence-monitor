package com.zhikuntech.intellimonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author 滕楠
 * @className FanScadaApplication
 * @create 2021/6/10 18:08
 **/
@SpringBootApplication
@EnableEurekaClient
public class FanScadaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FanScadaApplication.class,args);
    }
}