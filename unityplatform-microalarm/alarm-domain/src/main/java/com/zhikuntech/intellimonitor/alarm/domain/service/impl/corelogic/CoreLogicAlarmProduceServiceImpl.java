package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigMonitor;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicAlarmProduceService;
import com.zhikuntech.intellimonitor.core.commons.alarm.AlarmResultDTO;
import com.zhikuntech.intellimonitor.core.prototype.MonitorStructDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    private final IAlarmConfigMonitorService configMonitorService;

    private final IAlarmConfigRuleService configRuleService;


    public static final String REDIS_STATUS = "hash_cache_redis_status";

    public static final String REDIS_MONITOR = "hash_cache_redis_monitor";

    public static final String REDIS_RULE = "hash_cache_redis_rule";

    public void testRedisAccess() {

    }

    public void processAlarm() {
        // 0. kafka中获取原始数据
        final MonitorStructDTO monitorStructDTO = obtainRawMonitorData();
        // check -> todo 校验数据
        final String monitorNo = monitorStructDTO.getMonitorNo();
        final BigDecimal monitorValue = monitorStructDTO.getMonitorValue();

        // 1. 获取当前告警状态
        CurrentAlarmStatusDTO currentAlarmStatusDTO = obtainAlarmStatusByMonitorId(monitorNo);

        //#
        /*
            2. 判断当前数值是否产生相关告警
                2.1 如果数据时间戳小于当前存储的数据的时间戳 -> 直接丢弃该数据

                遥测数据
                遥信数据
         */

        // 获取测点的配置详情信息
        AlarmConfigMonitor alarmConfigMonitor = obtainMonitor(monitorNo);
        // todo 校验
        String ruleNo = alarmConfigMonitor.getRuleNo();
        // 获取规则规则详情信息
        AlarmConfigRule alarmConfigRule = obtainRule(ruleNo);
        // 判断告警(遥测数据 -> 告警/一级预警/二级预警/无告警)
        AlarmResultDTO alarmResultDTO = judgeAlarmOccur(monitorValue, alarmConfigRule);
        /* 是否产生了告警(1产生了告警) */
        @NotNull Integer produce = alarmResultDTO.getProduce();
        //#

        // 3. 记录状态变化




    }

    /**
     * kafka中获取原始数据
     * @return  测点数据
     */
    MonitorStructDTO obtainRawMonitorData() {
        // todo

        return null;
    }

    /**
     * 获取告警状态
     * @param monitorId     测点id
     * @return  告警状态
     */
    CurrentAlarmStatusDTO obtainAlarmStatusByMonitorId(String monitorId) {
        String cacheEntry = (String) redisTemplate.opsForHash().get(REDIS_STATUS, monitorId);
        if (Objects.isNull(cacheEntry)) {
            return null;
        }
        CurrentAlarmStatusDTO currentAlarmStatusDTO = null;
        try {
            currentAlarmStatusDTO = objectMapper.readValue(cacheEntry, CurrentAlarmStatusDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return currentAlarmStatusDTO;
    }

    /**
     * 获取测点配置
     * @param monitorId 测点id
     * @return
     */
    AlarmConfigMonitor obtainMonitor(String monitorId) {
        AlarmConfigMonitor alarmConfigMonitor = null;
        // read from cache
        String cacheEntry = (String) redisTemplate.opsForHash().get(REDIS_MONITOR, monitorId);
        if (StringUtils.isNotBlank(cacheEntry)) {
            try {
                alarmConfigMonitor = objectMapper.readValue(cacheEntry, AlarmConfigMonitor.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(alarmConfigMonitor)) {
                return alarmConfigMonitor;
            }
        }
        QueryWrapper<AlarmConfigMonitor> monitorQueryWrapper = new QueryWrapper<>();
        alarmConfigMonitor = configMonitorService.getBaseMapper().selectOne(monitorQueryWrapper);
        // load to cache
        if (Objects.nonNull(alarmConfigMonitor)) {
            try {
                String monitorNo = alarmConfigMonitor.getMonitorNo();
                String storeToRedis = objectMapper.writeValueAsString(alarmConfigMonitor);
                redisTemplate.opsForHash().put(REDIS_MONITOR, monitorNo, storeToRedis);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return alarmConfigMonitor;
    }

    /**
     * 获取规则配置
     * @param ruleNo    规则编码
     * @return
     */
    AlarmConfigRule obtainRule(/* 规则编码 */ String ruleNo) {
        AlarmConfigRule alarmConfigRule = null;
        final String ruleEntry = (String) redisTemplate.opsForHash().get(REDIS_RULE, ruleNo);
        if (Objects.nonNull(ruleEntry)) {
            try {
                alarmConfigRule = objectMapper.readValue(ruleEntry, AlarmConfigRule.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (Objects.isNull(alarmConfigRule)) {
            QueryWrapper<AlarmConfigRule> ruleQueryWrapper = new QueryWrapper<>();
            alarmConfigRule = configRuleService.getBaseMapper().selectOne(ruleQueryWrapper);
            // load to cache
            if (Objects.nonNull(alarmConfigRule)) {
                try {
                    String perRuleNo = alarmConfigRule.getRuleNo();
                    String ruleStr = objectMapper.writeValueAsString(alarmConfigRule);
                    redisTemplate.opsForHash().put(REDIS_RULE, perRuleNo, ruleStr);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return alarmConfigRule;
    }

    AlarmResultDTO judgeAlarmOccur(BigDecimal monitorNum, AlarmConfigRule alarmConfigRule) {
        // todo

        return null;
    }

    @Data
    public static class CurrentAlarmStatusDTO implements Serializable {

        private static final long serialVersionUID = -1354999979597391922L;

        /**
         * 测点id
         */
        private String monitorId;

        /**
         * 测点状态
         * 0 -> 无告警
         * 1 -> 一级预警
         * 2 -> 二级预警
         * 100 -> 告警
         */
        private Integer monitorStatus;

        /**
         * 测点类型
         */
        private Integer monitorType;
    }

}
