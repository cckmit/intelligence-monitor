package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.schedulefetch;

import com.zhikuntech.intellimonitor.fanscada.facade.feign.ToWindPowerForecast;
import com.zhikuntech.intellimonitor.mainpage.facade.MainPageFacade;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCapacity;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataZr;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCapacityService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCfService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataZrService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ConstantsOfWf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

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

    private final IWfDataCapacityService capacityService;


    @Override
    public void scheduleFetchCapacity() {
        LocalDateTime now = LocalDateTime.now();

        // fetch minute from now.
        String strNow = TimeProcessUtils.formatLocalDateTimeWithMinutePattern(now);
        LocalDateTime minutePattern = TimeProcessUtils.parseLocalDateTimeWithMinutePattern(strNow);

        // 252

        // 252 63 * 4
        int avtive = ThreadLocalRandom.current().nextInt(1, 64);


        WfDataCapacity dataCapacity = WfDataCapacity.builder()
                .orgId(ConstantsOfWf.DEV_ORG_ID)
                .createTime(now)
                .eventDateTime(minutePattern)
                .powerCalcCapacity(new BigDecimal(avtive * 4))
                .checkCalcCapacity(new BigDecimal("252"))
                .windPlatformGenElectric(new BigDecimal(avtive * 3))
                .status("00")
                .build();
        capacityService.getBaseMapper().insert(dataCapacity);
    }

    @Override
    public BigDecimal scheduleFetchMonthElectric() {
        // TODO 获取当月全场发电量
        int avtive = ThreadLocalRandom.current().nextInt(256, 300);
        return new BigDecimal(avtive);
    }

    @Override
    public void scheduleFetchActPower() {
//        ActWeatherDataDTO weatherData = mainPageFacade.getWeatherData();
//        log.info("调用mainPage接口获取数据:[{}]", weatherData);

        // 1min一条

        LocalDateTime now = LocalDateTime.now();

        // fetch minute from now.
        String strNow = TimeProcessUtils.formatLocalDateTimeWithMinutePattern(now);
        LocalDateTime minutePattern = TimeProcessUtils.parseLocalDateTimeWithMinutePattern(strNow);

        int windPower = ThreadLocalRandom.current().nextInt(1, 360);

        // Mock数据
        WfDataZr wfDataZr = WfDataZr.builder()
                .orgId(ConstantsOfWf.DEV_ORG_ID)
                .createTime(now)
                .actualProduce(new BigDecimal(windPower))
                .eventDateTime(minutePattern)
                .status(0)
                .fetchTime(now)
                .build();
        log.info("generate power data:[{}]", wfDataZr);
        // 存储数据
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
        // Mock数据
        int windDirect = ThreadLocalRandom.current().nextInt(1, 360);

        int windSpeed = ThreadLocalRandom.current().nextInt(1, 360);

        int pressure = ThreadLocalRandom.current().nextInt(1, 360);

        int temperature = ThreadLocalRandom.current().nextInt(1, 360);

        int humidity = ThreadLocalRandom.current().nextInt(1, 360);

        WfDataCf wfDataCf = WfDataCf.builder()
                .orgId(ConstantsOfWf.DEV_ORG_ID)
                .createTime(now)
                .windSpeed(new BigDecimal(windSpeed))
                .highLevel(new BigDecimal("70"))
                .windDirection(new BigDecimal(windDirect))
                .temperature(new BigDecimal(temperature))
                .humidity(new BigDecimal(humidity))
                .pressure(new BigDecimal(pressure))
                .turbineHigh(new BigDecimal("30"))
                .calcPower(new BigDecimal(windDirect * 0.618))
                .eventDateTime(minutePattern)
                .status(0)
                .fetchTime(now)
                .build();
        log.info("generate weather data:[{}]", wfDataCf);
        // 存储数据
        iWfDataCfService.save(wfDataCf);
    }




}
