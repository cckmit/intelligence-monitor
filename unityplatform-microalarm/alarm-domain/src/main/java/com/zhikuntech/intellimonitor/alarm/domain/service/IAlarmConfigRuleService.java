package com.zhikuntech.intellimonitor.alarm.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmRuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AddNewAlarmRuleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AlarmRuleSimpleQuery;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;

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

    /**
     * 添加新的告警规则
     *
     * @param query 添加条件
     * @return      true/false
     */
    boolean addNewAlarmRule(AddNewAlarmRuleQuery query);

    /**
     * 分页查询结果
     *
     * @param query 查询条件
     * @return      分页结果
     */
    Pager<AlarmRuleDTO> queryByPage(AlarmRuleSimpleQuery query);


    /**
     * 修改告警规则
     * @param query 待修改内容
     * @return 原样返回
     */
    AddNewAlarmRuleQuery changeRule(AddNewAlarmRuleQuery query);

}
