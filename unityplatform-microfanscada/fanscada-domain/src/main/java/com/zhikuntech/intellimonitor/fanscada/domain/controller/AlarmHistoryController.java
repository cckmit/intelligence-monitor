package com.zhikuntech.intellimonitor.fanscada.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.service.AlarmHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 滕楠
 * @className AlarmHistoryController
 * @create 2021/6/15 17:28
 **/
@RestController
@RequestMapping("alarmHistory")
@Api(tags = "报警历史记录")
public class AlarmHistoryController {

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    @ApiOperation("报警记录列表")
    @GetMapping("getList")
    public BaseResponse<List<Object>> getList(@RequestParam String date,
                                              @RequestParam String code,
                                              @RequestParam String description,
                                              @RequestParam String level){
        return alarmHistoryService.getList(date,code,description,level);
    }
}