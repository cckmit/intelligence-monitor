package com.zhikuntech.intellimonitor.cable.domain.controller;

import com.zhikuntech.intellimonitor.cable.domain.dto.CableStressAlarmDTO;
import com.zhikuntech.intellimonitor.cable.domain.dto.CableTemperatureAlarmDTO;
import com.zhikuntech.intellimonitor.cable.domain.query.AlarmQuery;
import com.zhikuntech.intellimonitor.cable.domain.query.CableIdQuery;
import com.zhikuntech.intellimonitor.cable.domain.service.CableAlarmService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cableAlarm")
@Api(tags = "海缆告警数据")
@Slf4j
public class CableAlarmController {
    @Autowired
    private CableAlarmService cableAlarmService;

    @PostMapping("/getTemperatureAlarm")
    @ApiOperation("海缆温度告警的前后12小时数据")
    public BaseResponse<List<CableTemperatureAlarmDTO>> getAlarmTemperature(@RequestBody CableIdQuery query) throws Exception {
        return BaseResponse.success(cableAlarmService.getAlarmTemperature(query));
    }

    @PostMapping("/getTemperatureAllAlarm")
    @ApiOperation("温度告警海缆告警时间的整条海缆的温度数据")
    public BaseResponse<List<CableTemperatureAlarmDTO>> getAlarmAllTemperature(@RequestBody CableIdQuery query) throws Exception {
        return BaseResponse.success(cableAlarmService.getAlarmAllTemperature(query));
    }

    @PostMapping("/getStressAlarm")
    @ApiOperation("海缆应力告警前后12小时数据")
    public BaseResponse<List<CableStressAlarmDTO>> getAlarmStress(@RequestBody CableIdQuery query) throws Exception {
        return BaseResponse.success(cableAlarmService.getAlarmStress(query));
    }

    @PostMapping("/getStressAllAlarm")
    @ApiOperation("应力告警海缆告警时间的整条海缆的应力数据")
    public BaseResponse<List<CableStressAlarmDTO>> getAlarmAllStress(@RequestBody CableIdQuery query) throws Exception {
        return BaseResponse.success(cableAlarmService.getAlarmAllStress(query));
    }
}
