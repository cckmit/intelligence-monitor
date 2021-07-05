package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.ActWeatherDataDTO;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.schedule.FanInfoInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 代志豪
 * 2021/6/15 20:02
 */
@RestController
@RequestMapping("/feign")
public class FeignController {

    @GetMapping("/getWeatherData")
    public ActWeatherDataDTO getCurrentData() throws Exception {
        ActWeatherDataDTO dto = new ActWeatherDataDTO();
        List<ValueData> valueData = GoldenUtil.getSnapshots(new int[]{1, 2, 3, 4, 5, 6});
        dto = InjectPropertiesUtil.injectByAnnotation(dto, null,valueData, (key) -> FanInfoInit.GOLDEN_ID_MAP.get(key));
        return dto;
    }
}
