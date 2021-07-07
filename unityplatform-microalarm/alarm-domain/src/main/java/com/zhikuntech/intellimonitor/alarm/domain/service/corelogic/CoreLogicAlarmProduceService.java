package com.zhikuntech.intellimonitor.alarm.domain.service.corelogic;

/**
 * 核心逻辑 -> 告警生成
 * <p>
 *     生成规则: (前提 -> 该测点产生预警或告警)
 *     1.不在相关告警 -> 直接生成
 *     2.已存在相关告警, 告警级别没有发生改变 -> 不生成告警
 *     3.已存在相关告警, 告警级别发生了改变  -> 生成告警, 之前告警进入历史
 * </p>
 *
 * @author liukai
 */
public interface CoreLogicAlarmProduceService {


}
