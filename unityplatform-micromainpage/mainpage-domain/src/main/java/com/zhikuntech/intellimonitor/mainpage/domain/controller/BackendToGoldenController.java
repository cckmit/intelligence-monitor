package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGolden;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQueryList;
import com.zhikuntech.intellimonitor.mainpage.domain.service.BackendToGoldenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/15 11:12
 * @Description mysql数据库和golden数据库关于点位的映射关系
 * @Version 1.0
 */
@Api(tags = "BackendToGoldenController",description = "mysql数据库和golden数据库关于点位的映射关系")
@RestController
@RequestMapping("/backendToGolden")
public class BackendToGoldenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendToGoldenController.class);

    @Autowired
    private BackendToGoldenService backendToGoldenService;

    @GetMapping("/getGoldenIdByBackendIdOrNumber")
    @ApiOperation("根据数据库表中编号或者风机编号查询golden数据库表中id（单条）")
    public BaseResponse getGoldenIdByBackendIdOrNumber(@RequestBody BackendToGoldenQuery backendToGoldenQuery){
        List<Integer> goldenIdList = backendToGoldenService.getGoldenIdByBackendIdOrNumber(backendToGoldenQuery);
        LOGGER.info("goldenIdList->{}",goldenIdList);
        return BaseResponse.success(goldenIdList);
    }

    @GetMapping("/listGoldenIdByBackendIdOrNumber")
    @ApiOperation("根据数据库表中编号或者风机编号查询golden数据库表中id（批量）")
    public BaseResponse listGoldenIdByBackendIdOrNumber(@RequestBody BackendToGoldenQueryList backendToGoldenQueryList){
        List<Integer> goldenIdList = backendToGoldenService.listGoldenIdByBackendIdOrNumber(backendToGoldenQueryList);
        LOGGER.info("goldenIdList->{}",goldenIdList);
        return BaseResponse.success(goldenIdList);
    }
}
