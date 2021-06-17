package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimePowerVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimeWindSpeedVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 18:37
 * @Description 风功率曲线
 * @Version 1.0
 */
@Api(tags = "WinPowerCurveController",description = "风功率曲线")
@RestController
@RequestMapping("/winPowerCurve")
public class WinPowerCurveController {
    @Autowired
    private WinPowerCurveService winPowerCurveService;

    @GetMapping("/getAll")
    @ApiOperation("获取所有时间内的【风功率曲线】数据")
    public BaseResponse<WindPowerCurveVO> getWindPowerCurveOfAllTime(){
        WindPowerCurveVO windPowerCurveVO = winPowerCurveService.getWindPowerCurveOfAllTime();
        return BaseResponse.success(windPowerCurveVO);
    }

    @GetMapping("/getCurrent")
    @ApiOperation("获取庚顿数据库中当前【风功率曲线】数据")
    public BaseResponse<WindPowerCurveVO> getWindPowerCurve() throws Exception {
        WindPowerCurveVO windPowerCurveVO = winPowerCurveService.getWindPowerCurve();
        return BaseResponse.success(windPowerCurveVO);
    }

    @GetMapping("/subscribe/{username}")
    @ApiOperation("订阅【风功率曲线】相关标签点快照改变的通知")
    @ApiImplicitParam(name = "username",value = "登录用户名")
    public BaseResponse<Boolean> subscribeWindPowerCurve(@PathVariable("username") String username) throws Exception {
        boolean result = winPowerCurveService.subscribeWindPowerCurve(username);
        return BaseResponse.success(result);
    }

    @GetMapping("/getShortTermForecastPower")
    @ApiOperation("获取短期预测功率")
    public BaseResponse getShortTermForecastPower(){
        List<TimePowerVO> shortTermForecastPowerList = winPowerCurveService.getShortTermForecastPower();
        return BaseResponse.success(shortTermForecastPowerList);
    }

    @GetMapping("/getSupShortTermForecastPower")
    @ApiOperation("获取超短期预测功率")
    public BaseResponse getSupShortTermForecastPower(){
        List<TimePowerVO> supShortTermForecastPowerList = winPowerCurveService.getSupShortTermForecastPower();
        return BaseResponse.success(supShortTermForecastPowerList);
    }

    @GetMapping("/getActualPower")
    @ApiOperation("获取实际功率")
    public BaseResponse getActualPower(){
        List<TimePowerVO> actualPowerList = winPowerCurveService.getActualPower();
        return BaseResponse.success(actualPowerList);
    }

    @GetMapping("/getWeatherForecastPower")
    @ApiOperation("获取天气预报风速")
    public BaseResponse getWeatherForecastPower(){
        List<TimeWindSpeedVO> weatherForecastPowerList = winPowerCurveService.getWeatherForecastPower();
        return BaseResponse.success(weatherForecastPowerList);
    }

    @GetMapping("/getMeasuredWindSpeed")
    @ApiOperation("获取实测风速")
    public BaseResponse getMeasuredWindSpeed(){
        List<TimeWindSpeedVO> measuredWindSpeedList = winPowerCurveService.getMeasuredWindSpeed();
        return BaseResponse.success(measuredWindSpeedList);
    }
}
