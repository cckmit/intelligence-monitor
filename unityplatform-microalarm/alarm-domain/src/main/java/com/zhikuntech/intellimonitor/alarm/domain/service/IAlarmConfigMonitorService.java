package com.zhikuntech.intellimonitor.alarm.domain.service;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigMonitor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmmonitor.AlarmMonitorSimpleQuery;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;

/**
 * <p>
 * 测点 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-07-05
 */
public interface IAlarmConfigMonitorService extends IService<AlarmConfigMonitor> {

    // TODO 查询所有测点

    // TODO 缓存所有测点


    /**
     * 查询所有分页后的结果
     *
     * @param query 查询条件
     * @return      分页后的结果
     */
    Pager<AlarmMonitorDTO> queryByPage(AlarmMonitorSimpleQuery query);


}
