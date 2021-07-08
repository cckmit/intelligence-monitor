package com.zhikuntech.intellimonitor.alarm.domain.controller.alarmquery;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmStatusGroupByModuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liukai
 */
@Api(tags = "告警查询相关")
@RestController
@RequestMapping("/alarm-query")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmQueryController {

    private final IAlarmProduceInfoService alarmProduceInfoService;


    @ApiOperation("查询所有模块告警数量")
    @GetMapping("/status-all-group")
    public BaseResponse<List<AlarmStatusGroupByModuleDTO>> fetchStatusAllGroup() {
        List<AlarmStatusGroupByModuleDTO> results = alarmProduceInfoService.fetchStatusAllGroup();
        return BaseResponse.success(results);
    }

}
