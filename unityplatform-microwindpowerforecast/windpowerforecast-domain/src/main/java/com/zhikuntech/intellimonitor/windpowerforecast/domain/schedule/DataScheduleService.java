package com.zhikuntech.intellimonitor.windpowerforecast.domain.schedule;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 数据调度服务
 *
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataScheduleService {

    private final RedissonClient redissonClient;

    private final ScheduleFetchDataService fetchDataService;


    //# cron -> 定时任务, 1min/次

    @Scheduled(cron = "30 * * * * ?")
    public void scheduleGenPower() {
        final RLock lock = redissonClient.getLock(ScheduleConstants.GEN_POWER_LOCK);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleGenPower");
                fetchDataService.scheduleFetchActPower();
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (enter) {
                lock.unlock();
            }
        }
    }

    @Scheduled(cron = "30 * * * * ?")
    public void scheduleGenWeather() {
        final RLock lock = redissonClient.getLock(ScheduleConstants.GEN_WEATHER_LOCK);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleGenWeather");
                fetchDataService.scheduleFetchActWeather();
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (enter) {
                lock.unlock();
            }
        }
    }

}
