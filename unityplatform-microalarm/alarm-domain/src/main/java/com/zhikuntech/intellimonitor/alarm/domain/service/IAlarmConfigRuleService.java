package com.zhikuntech.intellimonitor.alarm.domain.service;

import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 规则表 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
public interface IAlarmConfigRuleService extends IService<AlarmConfigRule> {

    /**
     * 查询等级数量
     *
     * @param levelNo   等级编码
     * @return          数据数量
     */
    Integer queryCountByLevelNo(String levelNo);
}
