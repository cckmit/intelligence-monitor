package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDto;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDto;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 代志豪
 * 2021/6/9 11:52
 */
@RestController
@RequestMapping("/fanInfo")
@Api(tags = "首页风机实时数据")
@Slf4j
public class FanInfoController {

    @Autowired
    private FanInfoService fanInfoService;

    @GetMapping("/getRuntimeInfo")
    @ApiOperation("获取当前最新风机运行时数据")
    public BaseResponse<List<FanRuntimeDto>> getRuntimeInfos() throws Exception {
        log.info("/getRuntimeInfo");
        return BaseResponse.success(fanInfoService.getRuntimeInfos());
    }

    @GetMapping("/getRuntimeInfo/{username}")
    @ApiOperation("websocket推送实时运行数据")
    public void getRuntimeInfos(@PathVariable String username) throws Exception {
        log.info("/getRuntimeInfo"+username);
        fanInfoService.getRuntimeInfos(username);
    }

    @GetMapping("/getStatistics")
    @ApiOperation("获取当前最新风场统计数据")
    public BaseResponse<FanStatisticsDto> getStatistics() throws Exception {
        return BaseResponse.success(new FanStatisticsDto());
    }

    @GetMapping("/getStatistics/{username}")
    @ApiOperation("websocket推送实时统计数据")
    public void getStatistics(@PathVariable String username) throws Exception {
        log.info("/getStatistics"+username);
        fanInfoService.getStatistics(username);
    }
}
