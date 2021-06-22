package com.zhikuntech.intellimonitor.windpowerforecast.domain.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
//    @Bean
//    public RedissonClient redisson() {
//        Config config = new Config();
//        //redis集群方式
//        config.useClusterServers().setScanInterval(2000)//设置集群状态扫描时间
//                .addNodeAddress("redis://127.0.0.1:7000")
//                .addNodeAddress("redis://127.0.0.1:7001")
//                .setPassword("123456");
//        return Redisson.create(config);
//    }
    /**
     * redis单机方式
     * @return
     */
    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        //redis单机方式
        config.useSingleServer()
                .setAddress("redis://192.168.3.179:6379");
        return Redisson.create(config);
    }

}
