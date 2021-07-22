package com.zhikuntech.intellimonitor.structuremonitor.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.structuremonitor.domain.query.StructureMonitoringQuery;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.IStructureMonitoringService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveSedimentationData;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveSpeedData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author： DAI
 * @date： Created in 2021/7/12 15:50
 */
@RestController
@RequestMapping("structureMonitoring")
@Api(tags = "结构监测")
public class StructureMonitoringController {

    @Resource
    private IStructureMonitoringService monitoringService;

    @PostMapping("list")
    public BaseResponse<List<StructureMonitoringQuery>> getList(@RequestBody StructureMonitoringQuery query) {
        // TODO
        return monitoringService.getList(query);
    }

    @GetMapping("getDataByType")
    @ApiOperation("获取实时速度数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "type", value = "1:最大 2:平均 3:最小"),
            @ApiImplicitParam(name = "fanNumber", value = "风机编号")
    })
    public BaseResponse<LiveSpeedData> getSpeedData(@RequestParam(defaultValue = "1") Integer type, @RequestParam Integer fanNumber) {

        return monitoringService.getSpeedData(type, fanNumber);
    }

    @GetMapping("getDataByType")
    @ApiOperation("获取实时沉降数据")
    @ApiImplicitParam(name = "fanNumber", value = "风机编号")
    public BaseResponse<LiveSedimentationData> getSedimentationData(@RequestParam Integer fanNumber) {

        return monitoringService.getSedimentationData(fanNumber);
    }
}
