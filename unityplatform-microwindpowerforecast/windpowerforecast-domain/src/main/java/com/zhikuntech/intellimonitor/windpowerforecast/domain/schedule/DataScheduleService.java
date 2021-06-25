package com.zhikuntech.intellimonitor.windpowerforecast.domain.schedule;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.constants.ScheduleConstants;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assesscalc.AssessCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.cdqcalc.CdqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.dqcalc.DqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private final AssessCalcService assessCalcService;

    private final DqCalcService dqCalcService;

    private final CdqCalcService cdqCalcService;

    /*计算服务*/

    // 考核结果-用电量(日数据/月数据)

    @Scheduled(cron = "0 0 2 1 * ?")
    public void scheduleGenCheckMonth() {
        // 每个月1号的凌晨2点钟触发
        // TODO

    }

    // 计算漏报次数

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleGenCheckDay() {
        // 每天凌晨一点钟触发
        String yesterdayStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assessCalcService.calcYesterdayAssess(yesterdayStr);
    }


    //# 统计分析-数据生成调度(短期/超短期)

    @Scheduled(cron = "0 2/15 * * * ?")
    public void scheduleGenAnalysis() {
        // 每天
        // TODO

    }

    /*   ----------------------数据获取调度----------------------   */

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
