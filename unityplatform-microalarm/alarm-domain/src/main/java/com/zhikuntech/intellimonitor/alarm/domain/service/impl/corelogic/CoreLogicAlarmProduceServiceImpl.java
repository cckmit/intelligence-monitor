package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigMonitor;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicAlarmProduceService;
import com.zhikuntech.intellimonitor.core.commons.alarm.AlarmResultDTO;
import com.zhikuntech.intellimonitor.core.commons.alarm.AlarmRuleDTO;
import com.zhikuntech.intellimonitor.core.commons.alarm.JudgeRuleWithAlarmUtil;
import com.zhikuntech.intellimonitor.core.prototype.MonitorStructDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

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
 *     告警恢复：(前提->存在告警)
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

    private final IAlarmProduceInfoService produceInfoService;


    public static final String REDIS_STATUS = "hash_cache_redis_status";

    public static final String REDIS_MONITOR = "hash_cache_redis_monitor";

    public static final String REDIS_RULE = "hash_cache_redis_rule";


    /**
     * 遥信数据
     */
    public static final Integer MONITOR_BOOLEAN = 0;

    /**
     * 遥测数据
     */
    public static final Integer MONITOR_NUM = 1;


    public static final BigDecimal BOOLEAN_ALARM = BigDecimal.valueOf(1);


    public void testRedisAccess() {

    }

    public void processAlarm() {
        // 0. kafka中获取原始数据
        final MonitorStructDTO monitorStructDTO = obtainRawMonitorData();
        if (Objects.isNull(monitorStructDTO)) {
            log.error("数据异常状态,获取的原始数据为null");
            return;
        }
        final String monitorNo = monitorStructDTO.getMonitorNo();
        final BigDecimal monitorValue = monitorStructDTO.getMonitorValue();
        final String monitorValueStr = monitorStructDTO.getMonitorValueStr();
        final Long eventTimeStamp = monitorStructDTO.getEventTimeStamp();
        if (Objects.isNull(monitorNo)
                || Objects.isNull(monitorValue) || Objects.isNull(monitorValueStr)
                || Objects.isNull(eventTimeStamp)) {
            log.error("数据(测点编号/测点值/测点时间戳)均不能为空,测点数据:[{}]", monitorStructDTO);
            return;
        }
        LocalDateTime eventDateTime = Instant.ofEpochMilli(eventTimeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();

        //#
        /*
            2. 判断当前数值是否产生相关告警
                2.1 如果数据时间戳小于当前存储的数据的时间戳 -> 直接丢弃该数据

                遥测数据
                遥信数据
         */

        // 获取测点的配置详情信息
        AlarmConfigMonitor alarmConfigMonitor = obtainMonitor(monitorNo);

        Integer monitorType = alarmConfigMonitor.getMonitorType();
        if (MONITOR_BOOLEAN.equals(monitorType)) {
            // 遥信数据 告警 -> 1告警 0不告警
            boolean alarmOccur = BOOLEAN_ALARM.equals(monitorValue);

            //# 告警信息处理




            //# 状态变更处理

            // 获取当前测点状态


            //# 状态变更处理

        } else if (MONITOR_NUM.equals(monitorType)){
            // 遥测数据 告警
            String ruleNo = alarmConfigMonitor.getRuleNo();
            // 获取规则规则详情信息
            AlarmConfigRule alarmConfigRule = obtainRule(ruleNo);
            // 判断告警(遥测数据 -> 告警/一级预警/二级预警/无告警)
            AlarmResultDTO alarmResultDTO = judgeAlarmOccur(monitorValue, alarmConfigRule);
            /* 是否产生了告警(1产生了告警) */
            @NotNull Integer produce = alarmResultDTO.getProduce();
            // check produce


            //# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~告警信息处理~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            // 查询当前告警信息 todo 此处需要在缓存中进行处理
            QueryWrapper<AlarmProduceInfo> produceInfoQueryWrapper = new QueryWrapper<>();
            produceInfoQueryWrapper.eq("monitor_no", monitorNo);
            produceInfoQueryWrapper.eq("has_restore", 0);
            produceInfoQueryWrapper.eq("with_history", 0);
            final AlarmProduceInfo alarmProduceInfo = produceInfoService.getBaseMapper().selectOne(produceInfoQueryWrapper);


            if (Integer.valueOf(1).equals(produce)) {
                // 告警生成(没有对应的告警信息)

                if (Objects.isNull(alarmProduceInfo)) {
                    // 不在相关告警 -> 直接生成
                    LocalDateTime curTime = LocalDateTime.now();
                    final AlarmProduceInfo produceInfo = AlarmProduceInfo.builder()
                            .version(0)
                            .monitorNo(monitorNo)
                            .groupType(alarmConfigMonitor.getGroupType())
                            .ruleNo(alarmConfigMonitor.getRuleNo())
                            .eventTime(eventDateTime)
                            .processTime(curTime)
                            .monitorNum(monitorValue)
                            .monitorNumStr(monitorValueStr)
                            .createTime(curTime)
                            .withHistory(0)
                            .preInfoNo(null)
                            .nextInfoNo(null)
                            .hasConfirm(0)
                            .confirmPerson(null)
                            .confirmTime(null)
                            .hasRestore(0)
                            .restoreTime(null)
                            .alarmDate(curTime)
                            .alarmTimestamp(curTime)
                            .build();
                    produceInfoService.getBaseMapper().insert(produceInfo);
                } else {
                    /*
                        已存在相关告警, 告警级别没有发生改变
                            1.如果未确认 -> 不生成告警
                            2.如果已确认 -> 生成相关告警
                     */

                    // 已存在相关告警, 告警级别发生了改变  -> 生成告警, 之前告警变为历史告警


                }
            }

            if (Integer.valueOf(0).equals(produce)) {
                // 告警恢复(存在对应的告警信息)

            }




            //# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~告警信息处理~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            //# --------------------------------------------------------状态变更处理--------------------------------------------------------

            // ------------------------提取当前状态------------------------
            Integer curStatus = null;
            if (MONITOR_NUM.equals(produce)) {
                switch (alarmResultDTO.getLevel()) {
                    case 1:
                        curStatus = 100;
                        break;
                    case 2:
                        curStatus = 1;
                        break;
                    case 3:
                        curStatus = 2;
                        break;
                    default:
                        // exception
                }
            } else {
                curStatus = 0;
            }
            if (Objects.isNull(curStatus)) {
                // 异常
                log.error("提取当前状态时出现异常状态,告警判断结果[{}],测点配置信息:[{}]", alarmResultDTO, alarmConfigMonitor);
                return;
            }
            // ------------------------提取当前状态------------------------

            // 获取当前测点状态
            MonitorStatusInfo monitorStatusInfo = obtainAlarmStatusByMonitorId(monitorNo);
            // 转换状态
            monitorStatusInfo.swapCurStatusAndPre(curStatus, eventTimeStamp);
            //# --------------------------------------------------------状态变更处理--------------------------------------------------------
        }

        //#


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
    MonitorStatusInfo obtainAlarmStatusByMonitorId(String monitorId) {
        String cacheEntry = (String) redisTemplate.opsForHash().get(REDIS_STATUS, monitorId);
        if (Objects.isNull(cacheEntry)) {
            return null;
        }
        MonitorStatusInfo monitorStatusInfo = null;
        try {
            monitorStatusInfo = objectMapper.readValue(cacheEntry, MonitorStatusInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return monitorStatusInfo;
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

    /**
     *
     * @param monitorNum        数值
     * @param alarmConfigRule   告警规则
     * @return  判断结果
     */
    AlarmResultDTO judgeAlarmOccur(BigDecimal monitorNum, AlarmConfigRule alarmConfigRule) {

        AlarmRuleDTO alarmRuleDTO = AlarmRuleDTO.builder()
                .alarmRange(alarmConfigRule.getAlarmValue())
                .preWarningRangeLevelOne(alarmConfigRule.getPreAlarmOneValue())
                .preWarningRangeLevelTwe(alarmConfigRule.getPreAlarmTwoValue())
                .build();

        String uuidMark = UUID.randomUUID().toString();
        log.info("[{}]告警判断调用,入参: num[{}], rule[{}]", uuidMark, monitorNum, alarmRuleDTO);
        AlarmResultDTO process = JudgeRuleWithAlarmUtil.process(monitorNum, alarmRuleDTO);
        log.info("[{}]告警判断调用结果,result:[{}]", uuidMark, process);
        return process;
    }

}
