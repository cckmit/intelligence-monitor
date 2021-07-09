package com.zhikuntech.intellimonitor.alarm.domain.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    // TODO 告警信息分页查询

    // TODO 告警确认（单个/批次/页面全部[groupType]）

    // TODO 事故追忆



    // ---------------------后台执行---------------------

    // TODO 告警恢复（后台执行, 无需查询条件）

    // TODO 告警生成

    // ---------------------后台执行---------------------





}
