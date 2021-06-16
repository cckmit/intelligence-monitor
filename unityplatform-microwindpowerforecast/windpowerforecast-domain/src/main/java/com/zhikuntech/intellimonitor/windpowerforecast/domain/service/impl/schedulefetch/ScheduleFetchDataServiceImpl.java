package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.schedulefetch;

import com.zhikuntech.intellimonitor.fanscada.facade.feign.ToWindPowerForecast;
import com.zhikuntech.intellimonitor.fanscada.prototype.dto.ActPowerDataDTO;
import com.zhikuntech.intellimonitor.mainpage.facade.MainPageFacade;
import com.zhikuntech.intellimonitor.mainpage.prototype.dto.ActWeatherDataDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleFetchDataServiceImpl implements ScheduleFetchDataService {

    private final MainPageFacade mainPageFacade;

    private final ToWindPowerForecast toWindPowerForecast;

    @Override
    public void scheduleFetchActPower() {
        ActWeatherDataDTO weatherData = mainPageFacade.getWeatherData();
        // TODO 存储数据

    }

    @Override
    public void scheduleFetchActWeather() {
        ActPowerDataDTO powerSum = toWindPowerForecast.getPowerSum();
        // TODO 存储数据

    }
}
