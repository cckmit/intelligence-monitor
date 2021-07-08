package com.zhikuntech.intellimonitor.alarm.domain.controller;


import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmmonitor.AlarmMonitorSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 测点 前端控制器
 * </p>
 *
 * @author liukai
 * @since 2021-07-05
 */
@Api(tags = "测点信息")
@RestController
@RequestMapping("/alarm-config-monitor")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigMonitorController {

    private final IAlarmConfigMonitorService monitorService;

    @ApiOperation("查询测点信息-分页")
    @PostMapping("/query-page")
    public BaseResponse<Pager<AlarmMonitorDTO>> queryByPage(@RequestBody AlarmMonitorSimpleQuery query) {
        Pager<AlarmMonitorDTO> results = monitorService.queryByPage(query);
        return BaseResponse.success(results);
    }

}
