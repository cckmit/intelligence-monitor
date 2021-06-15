package com.zhikuntech.intellimonitor.mainpage.facade;

import com.zhikuntech.intellimonitor.mainpage.prototype.dto.ActWeatherDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 代志豪
 * 2021/6/15 19:42
 */
@FeignClient(name = "mainpage-app")
public interface MainPageFacade {

    @GetMapping("/feign/getWeatherData")
    ActWeatherDataDTO getWeatherData();
}
