package com.zhikuntech.intellimonitor.cable.domain.controller;

import com.zhikuntech.intellimonitor.cable.domain.dto.CableRunStressTimeDTO;
import com.zhikuntech.intellimonitor.cable.domain.dto.CableRunTimeTemperatureDTO;
import com.zhikuntech.intellimonitor.cable.domain.service.CableRunTimeService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cable")
@Api(tags = "海缆实时数据")
@Slf4j
public class CableController {
    @Autowired
    private CableRunTimeService cableRunTimeService;

    @GetMapping("/getStress1")
    @ApiOperation("获取当前1号海缆应力实时数据")
    public BaseResponse<List<CableRunStressTimeDTO>> getRuntimeStress1() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeStress1());
    }
    @GetMapping("/getStress2")
    @ApiOperation("获取当前2号海缆应力实时数据")
    public BaseResponse<List<CableRunStressTimeDTO>> getRuntimeStress2() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeStress2());
    }
    @GetMapping("/getStress3")
    @ApiOperation("获取当前3号海缆应力实时数据")
    public BaseResponse<List<CableRunStressTimeDTO>> getRuntimeStress3() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeStress3());
    }
    @GetMapping("/getStressTest")
    @ApiOperation("获取海缆应力模拟数据")
    public BaseResponse<List<CableRunStressTimeDTO>> getRuntimeStressTest() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeStressTest());
    }
    @GetMapping("/getTemperatureTest")
    @ApiOperation("获取海缆温度模拟数据")
    public BaseResponse<List<CableRunTimeTemperatureDTO>> getRuntimeTemperatureTest() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeTemperatureTest());
    }
    @GetMapping("/getTemperature1")
    @ApiOperation("获取1号海缆温度实时数据")
    public BaseResponse<List<CableRunTimeTemperatureDTO>> getRuntimeTemperature1() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeTemperature1());
    }
    @GetMapping("/getTemperature2")
    @ApiOperation("获取2号海缆温度实时数据")
    public BaseResponse<List<CableRunTimeTemperatureDTO>> getRuntimeTemperature2() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeTemperature2());
    }
    @GetMapping("/getTemperature3")
    @ApiOperation("获取3号海缆温度实时数据")
    public BaseResponse<List<CableRunTimeTemperatureDTO>> getRuntimeTemperature3() throws Exception {
        return BaseResponse.success(cableRunTimeService.getRuntimeTemperature3());
    }
}
