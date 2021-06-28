package com.zhikuntech.intellimonitor.windpowerforecast.domain.schedule;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.constants.ScheduleConstants;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assesscalc.AssessCalcService;
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
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonthCalcAnalysisScheduleService {

    private final RedissonClient redissonClient;

    /*计算服务*/

    private final AssessCalcService assessCalcService;


    // 考核结果-用电量(日数据/月数据)

    @Scheduled(cron = "0 0 2 1 * ?")
    public void scheduleGenCheckMonth() {
        // 每个月1号的凌晨2点钟触发
        final RLock lock = redissonClient.getLock(ScheduleConstants.CALC_ASSESS_ELECTRIC);
        boolean enter = false;
        try {
            enter = lock.tryLock(0, 50, TimeUnit.SECONDS);
            if (enter) {
                log.info("schedule method: [{}]", "scheduleGenCheckMonth");
                LocalDate lastMonth = LocalDate.now().minusMonths(1);
                String lastMonthStr = lastMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01";
                assessCalcService.calcDayAndMonthAssessElectric(lastMonthStr);
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
