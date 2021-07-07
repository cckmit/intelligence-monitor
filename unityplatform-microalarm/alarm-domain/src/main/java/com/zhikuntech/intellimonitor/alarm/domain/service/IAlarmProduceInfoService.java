package com.zhikuntech.intellimonitor.alarm.domain.service;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmStatusGroupByModuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 告警信息 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
public interface IAlarmProduceInfoService extends IService<AlarmProduceInfo> {


    /**
     * 查询所有模块告警数量
     * @return 各组告警数量
     */
    List<AlarmStatusGroupByModuleDTO> fetchStatusAllGroup();

}
