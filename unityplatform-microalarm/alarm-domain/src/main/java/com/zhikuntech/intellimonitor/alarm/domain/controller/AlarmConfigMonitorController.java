package com.zhikuntech.intellimonitor.alarm.domain.controller;


import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/alarm-config-monitor")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigMonitorController {


    private final IAlarmConfigMonitorService monitorService;


    // TODO 查询所有测点



}
