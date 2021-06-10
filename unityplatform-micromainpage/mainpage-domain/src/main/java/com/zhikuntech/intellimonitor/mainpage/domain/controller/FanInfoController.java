package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.zhikuntech.intellimonitor.mainpage.domain.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDto;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDto;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/9 11:52
 */
@RestController
@RequestMapping("/fanInfo")
public class FanInfoController {

    @Autowired
    private FanInfoService fanInfoService;

    @GetMapping("/getRuntimeInfo")
    public BaseResponse<List<FanRuntimeDto>> getRuntimeInfos() throws Exception {
        return BaseResponse.success(fanInfoService.getRuntimeInfos());
    }

    @GetMapping("/getRuntimeInfo/{username}")
    public void getRuntimeInfos(@PathVariable String username) throws Exception {
        fanInfoService.getRuntimeInfos(username);
    }

    @GetMapping("/getStatistics")
    public BaseResponse<FanStatisticsDto> getStatistics() throws Exception {
        return BaseResponse.success(new FanStatisticsDto());
    }

    @GetMapping("/getStatistics/{username}")
    public void getStatistics(@PathVariable String username) throws Exception {

    }
}
