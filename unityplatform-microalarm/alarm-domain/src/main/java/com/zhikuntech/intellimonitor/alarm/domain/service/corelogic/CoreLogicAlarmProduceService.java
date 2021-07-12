package com.zhikuntech.intellimonitor.alarm.domain.service.corelogic;

/**
 * 核心逻辑 -> 告警生成
 * <p>
 *     生成规则: (当测点进行告警判断后产生预警或告警)
 *     1.不在相关告警 -> 直接生成
 *     2.已存在相关告警, 告警级别没有发生改变
 *          2.1 如果未确认 -> 不生成告警
 *          2.2 如果已确认 -> 生成相关告警
 *     3.已存在相关告警, 告警级别发生了改变  -> 生成告警, 之前告警变为历史告警
 * </p>
 *
 * <p>
 *     告警恢复：
 *      告警恢复
 *          1.需要恢复当前告警及历史告警
 *          2.是根据当前策略进行判断【即不需要存储历史策略】
 * </p>
 *
 * @author liukai
 */
public interface CoreLogicAlarmProduceService {


}
