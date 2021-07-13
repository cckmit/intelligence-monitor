package com.zhikuntech.intellimonitor.structuremonitor.domain.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 滕楠
 * @className MystartUp
 * @create 2021/7/9 14:28
 **/
@Component
@Order
public class MyStartUp implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        //todo 初始数据获取
    }
}