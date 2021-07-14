package com.zhikuntech.intellimonitor.alarm.domain.controller.external;

import com.zhikuntech.intellimonitor.alarm.domain.dto.external.CurrentStatusByMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.external.FetchAlarmNumWithGroupDTO;
import com.zhikuntech.intellimonitor.alarm.domain.service.external.ProvideToExternalAlarmInfoService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给外部的接口
 * <p>
 *     该外部的接口
 * </p>
 *
 * @author liukai
 */
@Api(tags = "提供给外部的接口")
@RestController
@RequestMapping("/external")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProvideToExternalAlarmInfoController {


    private final ProvideToExternalAlarmInfoService externalAlarmInfoService;

    /*
         1.获取报警组对应的报警数量
         2.初始状态--获取页面上所有测点的状态

        {
            "code": 200,
            "msg": "",
            "data": []
        }
     */

    @ApiOperation("获取报警组对应的报警数量")
    @PostMapping("/fetch-alarm-num-by-group")
    public BaseResponse<List<FetchAlarmNumWithGroupDTO>> fetchWithGroup(@RequestBody List<String> groupNos) {
        return BaseResponse.success(externalAlarmInfoService.fetchWithGroup(groupNos));
    }

    @ApiOperation("获取页面上所有测点的状态")
    @PostMapping("/fetch-current-status")
    public BaseResponse<List<CurrentStatusByMonitorDTO>> fetchMonitorCurrentStatus(@RequestBody List<String> monitorIds) {
        return BaseResponse.success(externalAlarmInfoService.fetchMonitorCurrentStatus(monitorIds));
    }


}
