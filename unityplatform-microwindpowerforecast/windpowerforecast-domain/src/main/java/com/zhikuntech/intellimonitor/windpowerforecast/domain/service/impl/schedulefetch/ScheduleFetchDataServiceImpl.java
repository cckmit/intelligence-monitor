package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.schedulefetch;

import com.zhikuntech.intellimonitor.fanscada.facade.feign.ToWindPowerForecast;
import com.zhikuntech.intellimonitor.mainpage.facade.MainPageFacade;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataZr;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataCfService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataZrService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ConstantsOfWf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleFetchDataServiceImpl implements ScheduleFetchDataService {

    private final MainPageFacade mainPageFacade;

    private final ToWindPowerForecast toWindPowerForecast;

    private final IWfDataZrService iWfDataZrService;

    private final IWfDataCfService iWfDataCfService;

    @Override
    public void scheduleFetchActPower() {
//        ActWeatherDataDTO weatherData = mainPageFacade.getWeatherData();
//        log.info("调用mainPage接口获取数据:[{}]", weatherData);

        // 1min一条

        LocalDateTime now = LocalDateTime.now();

        // fetch minute from now.
        String strNow = TimeProcessUtils.formatLocalDateTimeWithMinutePattern(now);
        LocalDateTime minutePattern = TimeProcessUtils.parseLocalDateTimeWithMinutePattern(strNow);

        // TODO Mock数据
        WfDataZr wfDataZr = WfDataZr.builder()
                .orgId(ConstantsOfWf.DEV_ORG_ID)
                .createTime(now)
                .actualProduce(new BigDecimal("100"))
                .eventDateTime(minutePattern)
                .status(0)
                .fetchTime(now)
                .build();
        log.info("generate power data:[{}]", wfDataZr);
        // TODO 存储数据
        iWfDataZrService.save(wfDataZr);
    }

    @Override
    public void scheduleFetchActWeather() {
//        ActPowerDataDTO powerSum = toWindPowerForecast.getPowerSum();
//        log.info("调用Scada接口获取数据:[{}]", powerSum);

        // 1min 一条
        LocalDateTime now = LocalDateTime.now();

        String strNow = TimeProcessUtils.formatLocalDateTimeWithMinutePattern(now);
        LocalDateTime minutePattern = TimeProcessUtils.parseLocalDateTimeWithMinutePattern(strNow);
        // TODO Mock数据
        WfDataCf wfDataCf = WfDataCf.builder()
                .orgId(ConstantsOfWf.DEV_ORG_ID)
                .createTime(now)
                .windSpeed(new BigDecimal("45"))
                .highLevel(new BigDecimal("70"))
                .windDirection(new BigDecimal("45"))
                .temperature(new BigDecimal("45"))
                .humidity(new BigDecimal("45"))
                .pressure(new BigDecimal("45"))
                .turbineHigh(new BigDecimal("30"))
                .eventDateTime(minutePattern)
                .status(0)
                .fetchTime(now)
                .build();
        log.info("generate weather data:[{}]", wfDataCf);
        // TODO 存储数据
        iWfDataCfService.save(wfDataCf);
    }

    //# cron -> 定时任务, 1min/次

//    @Scheduled(cron = "30 * * * * ?")
    public void scheduleGenPower() {
        log.info("schedule method: [{}]", "scheduleGenPower");
        scheduleFetchActPower();
    }

//    @Scheduled(cron = "30 * * * * ?")
    public void scheduleGenWeather() {
        log.info("schedule method: [{}]", "scheduleGenWeather");
        scheduleFetchActWeather();
    }

}
