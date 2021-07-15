package com.zhikuntech.intellimonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 滕楠
 * @className StructureDataApplication
 * @create 2021/7/15 11:42
 **/
@SpringBootApplication
@EnableScheduling
public class StructureDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(StructureDataApplication.class,args);
    }
}