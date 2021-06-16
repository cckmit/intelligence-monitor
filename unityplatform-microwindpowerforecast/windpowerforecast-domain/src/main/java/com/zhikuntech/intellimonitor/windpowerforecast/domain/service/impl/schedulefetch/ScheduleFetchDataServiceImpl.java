package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.schedulefetch;

import com.zhikuntech.intellimonitor.mainpage.facade.MainPageFacade;
import com.zhikuntech.intellimonitor.mainpage.prototype.dto.ActWeatherDataDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch.ScheduleFetchDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liukai
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleFetchDataServiceImpl implements ScheduleFetchDataService {

    private final MainPageFacade mainPageFacade;

    @Override
    public void scheduleFetchActPower() {
        ActWeatherDataDTO weatherData = mainPageFacade.getWeatherData();
        // TODO 存储数据

    }

    @Override
    public void scheduleFetchActWeather() {

    }
}
