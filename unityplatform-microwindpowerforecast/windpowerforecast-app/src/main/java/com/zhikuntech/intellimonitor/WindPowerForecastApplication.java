package com.zhikuntech.intellimonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 风功率预测
 *
 * @author liukai
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class WindPowerForecastApplication {

    public static void main(String[] args) {

        SpringApplication.run(WindPowerForecastApplication.class);
    }
}
