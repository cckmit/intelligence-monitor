package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
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
    public BaseResponse<WeatherHighDTO> fetchHigh() {
        WeatherHighDTO weatherHighDTO = new WeatherHighDTO();
        weatherHighDTO.setActHigh(Arrays.asList(new BigDecimal("10"), new BigDecimal("20"), new BigDecimal("30")));
        weatherHighDTO.setVirtualHigh(Arrays.asList(new BigDecimal("10"), new BigDecimal("25"), new BigDecimal("55")));
        return BaseResponse.success(weatherHighDTO);
    }

    /*
        TODO 曲线展示(列表模式) -> 待确认: 以哪个为基准(短期预测功率/超短期预测功率) [时间为15min]
            查询参数:
                时间
                数字天气预报
                实测气象

        TODO 曲线展示（曲线模式）-> 数据是否一致


        TODO 日发电量计算

     */


    //# 实测气象数据

    /*
        TODO 风向玫瑰图
        TODO 风能玫瑰图

        待确定问题,正好落在 N/NNE/NE等上面该如何计算
        待确定夹角问题


        16个方位:
            N
            NNE
            NE
            ENE
            E
            ESE
            SE
            SSE
            S
            SSW
            SW
            WSW
            W
            WNW
            NW
            NNW

     */





    /*
        TODO    列表模式
            参数: 时间  高度

     */


    //# 实测气象数据

}
