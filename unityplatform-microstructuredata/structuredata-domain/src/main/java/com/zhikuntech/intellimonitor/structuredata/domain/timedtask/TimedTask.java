package com.zhikuntech.intellimonitor.structuredata.domain.timedtask;

import com.zhikuntech.intellimonitor.structuredata.domain.entity.ShakeData;
import com.zhikuntech.intellimonitor.structuredata.domain.service.ShakeDataService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 滕楠
 * @className TimedTask
 * @create 2021/7/15 10:58
 **/
@Component
public class TimedTask {

@Resource
private ShakeDataService shakeDataService;
    @Scheduled(cron = "0 */1 * * * *?")
    public void getFanData(){
        ShakeData fanData = shakeDataService.getFanData("1");
        System.out.println(fanData);
    }
}