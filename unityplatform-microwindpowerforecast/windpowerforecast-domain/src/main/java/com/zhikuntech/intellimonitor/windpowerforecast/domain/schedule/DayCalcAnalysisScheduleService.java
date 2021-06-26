package com.zhikuntech.intellimonitor.windpowerforecast.domain.schedule;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.constants.ScheduleConstants;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assesscalc.AssessCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.cdqcalc.CdqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.dqcalc.DqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
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
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DayCalcAnalysisScheduleService {


    private final RedissonClient redissonClient;

    /*计算服务*/

    private final AssessCalcService assessCalcService;

    private final DqCalcService dqCalcService;

    private final CdqCalcService cdqCalcService;


    // 计算漏报次数

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleGenCheckDay() {
        // 每天凌晨一点钟触发

        final RLock lock = redissonClient.getLock(ScheduleConstants.CALC_HIATUS);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleGenCheckDay");
                String yesterdayStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                assessCalcService.calcYesterdayAssess(yesterdayStr);
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

    //# 统计分析-数据生成调度(短期/超短期)

    @Scheduled(cron = "0 2/15 * * * ?")
    public void scheduleGenAnalysis() {
        // 每天
        final RLock lock = redissonClient.getLock(ScheduleConstants.ANA_DATA_LOCK);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleGenAnalysis");
                LocalDateTime dayBg = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime headerDateTime = dayBg.plusMinutes(15);

                String dayBgStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(dayBg);
                String nowStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(now);
                String headerDateTimeStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(headerDateTime);

                dqCalcService.dqDataCalc(dayBgStr, nowStr, headerDateTimeStr);
                cdqCalcService.calcData(dayBgStr, nowStr, headerDateTimeStr);
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
