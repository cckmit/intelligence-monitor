package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicAlarmProduceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * <p>
 *
 * </p>
 *
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoreLogicAlarmProduceServiceImpl implements CoreLogicAlarmProduceService {


    void processAlarm() {
        // 1. 获取当前告警状态 (暂时从数据库中获取)

        /*
            2. 判断当前数值是否产生相关告警
                2.1

                遥测数据
                遥信数据
         */


        // 3. 记录状态变化



    }



}
