package com.zhikuntech.intellimonitor.integratedautomation.domain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.integratedautomation.domain.po.AlarmConfigMonitorPO;
import com.zhikuntech.intellimonitor.integratedautomation.domain.service.AlarmConfigMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/7/22 11:28
 * @Description 告警配置
 * @Version 1.0
 */
@Api("告警配置")
@RequestMapping("/alarmConfig")
@RestController
@Slf4j
public class AlarmConfigMonitorController {
    @Autowired
    private AlarmConfigMonitorService alarmConfigMonitorService;

    /**
     * 根据页面标识查询庚顿数据库id
     * @param pageIdentifies
     * @return
     */
    @ApiOperation("根据页面标识查询庚顿数据库id")
    @GetMapping("/getGoldenIdByPageIdentifies")
    public BaseResponse getGoldenIdByPageIdentifies(String[] pageIdentifies){
        log.info("入参 页面标识->{}",pageIdentifies);
        QueryWrapper<AlarmConfigMonitorPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("monitor_no").in("logic_partition",pageIdentifies);
        List<AlarmConfigMonitorPO> list = alarmConfigMonitorService.list(queryWrapper);
        Object[] goldenIds = list.stream().map(AlarmConfigMonitorPO::getMonitorNo).toArray();
        return BaseResponse.success(goldenIds);
    }
}
