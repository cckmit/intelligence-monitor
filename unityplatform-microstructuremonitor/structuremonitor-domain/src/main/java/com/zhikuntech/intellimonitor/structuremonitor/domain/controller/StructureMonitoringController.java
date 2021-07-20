package com.zhikuntech.intellimonitor.structuremonitor.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.structuremonitor.domain.query.StructureMonitoringQuery;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.IStructureMonitoringService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
    @ApiOperation("获取实时数据")
    @ApiImplicitParam(name = "type",value = "页签类型:1,最大,2,平均,3,最小")
    public BaseResponse<List<LiveData>> getData(@RequestParam String type,@RequestParam Integer fanNumber) {
       return monitoringService.getData(type,fanNumber);
    }



}
