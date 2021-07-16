package com.zhikuntech.intellimonitor.alarm.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmInfoBatchDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmInfoDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmStatusGroupByModuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmConfirmQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmInfoLimitQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo.AlarmInfoSimpleQuery;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;

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

    /**
     * 分页查询告警信息
     *
     * @param simpleQuery   查询条件
     * @return  分页结果
     */
    Pager<AlarmInfoDTO> queryByPage(AlarmInfoSimpleQuery simpleQuery);


    /**
     * 滚动条滚动时查询批次 (取代分页查询)
     *
     * @param limitQuery    查询条件
     * @return              查询结果
     */
    List<AlarmInfoBatchDTO> fetchBatchLimit(AlarmInfoLimitQuery limitQuery);

    /**
     * 告警信息确认
     *
     * @param query 查询条件
     * @return 确认结果
     */
    boolean alarmConfirm(AlarmConfirmQuery query);

}
