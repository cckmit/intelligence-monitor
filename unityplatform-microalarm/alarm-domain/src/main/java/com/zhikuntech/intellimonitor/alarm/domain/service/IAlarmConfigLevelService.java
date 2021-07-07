package com.zhikuntech.intellimonitor.alarm.domain.service;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigLevel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AddNewAlarmLevelQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AlarmLevelSimpleQuery;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;

/**
 * <p>
 * 告警等级表 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
public interface IAlarmConfigLevelService extends IService<AlarmConfigLevel> {

    /**
     * 新增告警等级
     * @param query 新增内容
     * @return true 新增成功 false 新增失败
     */
    boolean addNewAlarmLevel(AddNewAlarmLevelQuery query);

    /**
     * 查询告警等级-分页
     * @param query 查询(页码+每页数量)
     * @return 分页后的数据
     */
    Pager<AlarmLevelDTO> queryAlarmLevel(AlarmLevelSimpleQuery query);

}
