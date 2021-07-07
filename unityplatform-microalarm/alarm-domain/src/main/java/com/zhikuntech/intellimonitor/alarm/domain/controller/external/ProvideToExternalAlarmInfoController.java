package com.zhikuntech.intellimonitor.alarm.domain.controller.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给外部的接口
 *
 * @author liukai
 */
@RestController
@RequestMapping("/external")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProvideToExternalAlarmInfoController {

    /*
        TODO
         1.获取报警组对应的报警数量
         2.初始状态--获取页面上所有测点的状态
         3.获取有状态变化的测点的状态    -> 需求不明 (有状态变化)

     */



}
