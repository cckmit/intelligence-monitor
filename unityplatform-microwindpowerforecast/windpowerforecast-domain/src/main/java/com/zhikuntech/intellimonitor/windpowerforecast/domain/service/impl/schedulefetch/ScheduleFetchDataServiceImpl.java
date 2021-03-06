package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.schedulefetch;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.fanscada.facade.feign.ToWindPowerForecast;
import com.zhikuntech.intellimonitor.mainpage.facade.MainPageFacade;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCapacity;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataZr;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCapacityService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCfService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataZrService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.constants.ConstantsOfWf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
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
        // TODO ???????????????????????????
        int avtive = ThreadLocalRandom.current().nextInt(256, 300);
        return new BigDecimal(avtive);
    }

    @Override
    public void scheduleFetchActPower() {

        // 1min??????

        LocalDateTime now = LocalDateTime.now();

        // fetch minute from now.
        String strNow = TimeProcessUtils.formatLocalDateTimeWithMinutePattern(now);
        LocalDateTime minutePattern = TimeProcessUtils.parseLocalDateTimeWithMinutePattern(strNow);


        // ???????????????????????????
        QueryWrapper<WfDataZr> zrQueryWrapper = new QueryWrapper<>();
        String minutePatternStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(minutePattern);
        zrQueryWrapper.eq("event_date_time", minutePatternStr);
        WfDataZr existJudge = iWfDataZrService.getBaseMapper().selectOne(zrQueryWrapper);
        if (Objects.nonNull(existJudge)) {
            log.warn("????????????????????????:[{}]???????????????.", minutePatternStr);
        }


        int windPower = ThreadLocalRandom.current().nextInt(1, 360);

        // Mock??????
        WfDataZr wfDataZr = WfDataZr.builder()
                .orgId(ConstantsOfWf.DEV_ORG_ID)
                .createTime(now)
                .actualProduce(new BigDecimal(windPower))
                .eventDateTime(minutePattern)
                .status(0)
                .fetchTime(now)
                .build();
        log.info("generate power data:[{}]", wfDataZr);
        // ????????????
        iWfDataZrService.save(wfDataZr);
    }

    @Override
    public void scheduleFetchActWeather() {
//        ActPowerDataDTO powerSum = toWindPowerForecast.getPowerSum();
//        log.info("??????Scada??????????????????:[{}]", powerSum);

        // 1min ??????
        LocalDateTime now = LocalDateTime.now();

        String strNow = TimeProcessUtils.formatLocalDateTimeWithMinutePattern(now);
        LocalDateTime minutePattern = TimeProcessUtils.parseLocalDateTimeWithMinutePattern(strNow);
        // Mock??????
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
        // ????????????
        iWfDataCfService.save(wfDataCf);
    }




}
