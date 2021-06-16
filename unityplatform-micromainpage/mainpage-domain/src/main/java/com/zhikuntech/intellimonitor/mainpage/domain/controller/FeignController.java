package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.ActWeatherDataVO;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.BeanConvertUtils;
import com.zhikuntech.intellimonitor.mainpage.prototype.dto.ActWeatherDataDTO;
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

    @Autowired
    private GoldenUtil goldenUtil;

    @GetMapping("/getWeatherData")
    public ActWeatherDataDTO getCurrentData() throws Exception {
        ActWeatherDataVO dto = new ActWeatherDataVO();
        List<ValueData> valueData = goldenUtil.getSnapshots(new int[]{1, 2, 3, 4, 5, 6});
        ActWeatherDataVO actWeatherDataVO = InjectPropertiesUtil.injectByAnnotation(dto, valueData);
        return BeanConvertUtils.copyProperties(actWeatherDataVO, ActWeatherDataDTO.class);
    }
}
