package com.zhikuntech.intellimonitor.alarm.domain.controller;


import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmInfoBatchDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmInfoDTO;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmConfirmQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmInfoLimitQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmInfoSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
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

import java.util.List;

/**
 * <p>
 * 告警信息 前端控制器
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Api(tags = "告警信息")
@RestController
@RequestMapping("/alarm-produce-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmProduceInfoController {


    private final IAlarmProduceInfoService infoService;

    @Deprecated
    @ApiOperation("(废弃的接口)告警信息分页查询-[告警信息查询暂时不需要分页查询]")
    @PostMapping("/query-by-page")
    public BaseResponse<Pager<AlarmInfoDTO>> queryByPage(@RequestBody AlarmInfoSimpleQuery simpleQuery) {
        Pager<AlarmInfoDTO> pageResult = infoService.queryByPage(simpleQuery);
        return BaseResponse.success(pageResult);
    }

    @ApiOperation("根据行号查询")
    @PostMapping("/query-batch-limit")
    public BaseResponse<List<AlarmInfoBatchDTO>> fetchBatchLimit(@RequestBody AlarmInfoLimitQuery limitQuery) {
        List<AlarmInfoBatchDTO> results = infoService.fetchBatchLimit(limitQuery);
        return BaseResponse.success(results);
    }


    @ApiOperation("告警确认（单个/批次/页面全部[groupType]）")
    @PostMapping("/alarm-confirm")
    public BaseResponse<Boolean> alarmConfirm(@RequestBody AlarmConfirmQuery query) {
        boolean result = infoService.alarmConfirm(query);
        return BaseResponse.success(result);
    }


    // TODO 事故追忆



    // ---------------------后台执行---------------------

    // TODO 告警恢复（后台执行, 无需查询条件）

    // TODO 告警生成

    // ---------------------后台执行---------------------





}
