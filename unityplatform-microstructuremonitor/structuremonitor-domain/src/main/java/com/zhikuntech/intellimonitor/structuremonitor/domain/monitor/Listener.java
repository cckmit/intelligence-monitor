package com.zhikuntech.intellimonitor.structuremonitor.domain.monitor;

import com.mysql.cj.x.protobuf.MysqlxExpr;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 滕楠
 * @className listener
 * @create 2021/7/9 14:30
 **/
@Component
public class Listener {

    /**
     * 获取结构监测所需要的数据 每分钟一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void getDataFrom() {

    }
}