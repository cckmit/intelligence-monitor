package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.WeatherHighDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 常用展示
 *
 * @author liukai
 */
@Api(tags = "常用展示")
@RestController
@RequestMapping("/normal-usage-play")
public class NormalUsagePlayController {


    // TODO 查询实测气象高度, 预测气象高度


    @ApiOperation("查询实测气象高度, 预测气象高度")
    @GetMapping("/high")
    public WeatherHighDTO fetchHigh() {
        WeatherHighDTO weatherHighDTO = new WeatherHighDTO();
        weatherHighDTO.setActHigh(Arrays.asList(new BigDecimal("10"), new BigDecimal("20"), new BigDecimal("30")));
        weatherHighDTO.setVirtualHigh(Arrays.asList(new BigDecimal("10"), new BigDecimal("25"), new BigDecimal("55")));
        return weatherHighDTO;
    }

}
