package com.zhikuntech.intellimonitor.windpowerforecast.domain.schedule;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.constants.ScheduleConstants;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfBasicParseResultService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.CalcCommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    /*计算服务*/

    private final IWfBasicParseResultService parseResultService;

    /*   ----------------------预测数据数据获取调度----------------------   */

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduleDqDataFetch() {
        // 每日凌晨两点触发

        RLock lock = redissonClient.getLock(ScheduleConstants.FOREST_DQ_LOCK);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleDqDataFetch");
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                parseResultService.fetchDqWithPointDate(dateTime, "dq");
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            if (enter) {
                lock.unlock();
            }
        }

    }

    @Scheduled(cron = "30 0/15 * * * ?")
    public void scheduleCdqDataFetch() {
        RLock lock = redissonClient.getLock(ScheduleConstants.FOREST_CDQ_LOCK);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleDqDataFetch");
                LocalDateTime now = LocalDateTime.now();
                // now 每隔15分钟处理一次, 时间落点问题
                now = CalcCommonUtils.timePostRangeProcessRetDateTime.apply(now);
                parseResultService.fetchDqWithPointDate(now, "cdq");
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            if (enter) {
                lock.unlock();
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleNwpDataFetch() {
        // 每日凌晨一点触发
        RLock lock = redissonClient.getLock(ScheduleConstants.FOREST_NWP_LOCK);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleNwpDataFetch");
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                parseResultService.fetchDqWithPointDate(dateTime, "nwp");
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            if (enter) {
                lock.unlock();
            }
        }
    }

    /*   ----------------------实时数据数据获取调度----------------------   */

    //# cron -> 定时任务, 1min/次

    @Scheduled(cron = "30 0/15 * * * ?")
    public void scheduleFetchCap() {
        final RLock lock = redissonClient.getLock(ScheduleConstants.GEN_CAP_LOCK);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleGenPower");
                fetchDataService.scheduleFetchCapacity();
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
