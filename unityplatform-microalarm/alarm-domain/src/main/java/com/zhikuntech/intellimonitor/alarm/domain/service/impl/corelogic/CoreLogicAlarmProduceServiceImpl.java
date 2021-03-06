package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigLevel;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigMonitor;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigLevelService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicAlarmProduceService;
import com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event.AlarmRelieveEvent;
import com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event.StatusDataReceiveEvent;
import com.zhikuntech.intellimonitor.core.commons.alarm.AlarmResultDTO;
import com.zhikuntech.intellimonitor.core.commons.alarm.AlarmRuleDTO;
import com.zhikuntech.intellimonitor.core.commons.alarm.JudgeRuleWithAlarmUtil;
import com.zhikuntech.intellimonitor.core.commons.utils.SnowflakeArithmeticUtil;
import com.zhikuntech.intellimonitor.core.prototype.MonitorStructDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

import static com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.MonitorStatusInfo.SIGNAL_DATA_ALARM;
import static com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.MonitorStatusInfo.SIGNAL_DATA_NO_ALARM;

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
public class CoreLogicAlarmProduceServiceImpl implements CoreLogicAlarmProduceService, ApplicationContextAware {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    private final IAlarmConfigMonitorService configMonitorService;

    private final IAlarmConfigRuleService configRuleService;

    private final IAlarmProduceInfoService produceInfoService;

    private final IAlarmConfigLevelService levelService;

    /**
     * 遥信数据
     */
    public static final Integer MONITOR_BOOLEAN = 0;

    /**
     * 遥测数据
     */
    public static final Integer MONITOR_NUM = 1;


    public static final BigDecimal BOOLEAN_ALARM = BigDecimal.valueOf(1);

    public static final Integer NUM_ALARM = 1;


    /**
     * use this to publish event
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void testRedisAccess() {

    }


    @Override public void processAlarm() {
        final MonitorStructDTO monitorStructDTO = obtainRawMonitorData();
        processAlarm(monitorStructDTO);
    }

    @Override public void processAlarm(final MonitorStructDTO monitorStructDTO) {

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
            // 获取当前告警信息
            AlarmProduceInfo produceInfo = produceInfoService.fetchCurAlarmInfoByMonitorNo(monitorNo);
            if /* 产生告警 */ (alarmOccur) {
                if /* 不存在当前告警信息 */ (Objects.isNull(produceInfo)) {
                    // 1.生成告警信息并保存
                    LocalDateTime curTime = LocalDateTime.now();
                    String levelCur = fetchAlarmLevelByDataTypeAndLevel(alarmConfigMonitor.getRuleNo(), "bool", null);
                    AlarmProduceInfo produceAlarmInfo = AlarmProduceInfo.builder()
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
                            .chainInfo(UUID.randomUUID().toString())
                            .nextInfoNo(null)
                            .hasConfirm(0)
                            .confirmPerson(null)
                            .confirmTime(null)
                            .hasRestore(0)
                            .restoreTime(null)
                            .alarmDate(curTime)
                            .alarmTimestamp(curTime)
                            .rowStamp(genRow())
                            // todo 告警描述
                            .alarmLevel(levelCur)
                            .build();
                    int insert = produceInfoService.getBaseMapper().insert(produceAlarmInfo);
                    assert insert == 1;
                    // 2.状态机制
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            SIGNAL_DATA_ALARM
                    ));
                } /* 存在当前告警信息 */ else {
                    //# --------------------获取告警等级--------------------
                    // 获取当前规则
                    String ruleNo = produceInfo.getRuleNo();
                    AlarmConfigRule configRule = obtainRule(ruleNo);
                    String levelNoForAlarm = configRule.getLevelNoAlarm();
                    // 获取告警等级
                    AlarmConfigLevel alarmConfigLevel = obtainAlarmLevelByLevelNo(levelNoForAlarm);
                    final String alarmLevelCur = alarmConfigLevel.getLevelIllustrate();
                    //# --------------------获取告警等级--------------------

                    // 1.告警级别是否发生改变(同当前告警策略进行比较)
                    String alarmLevel = produceInfo.getAlarmLevel();
                    if /* 告警等级没有发生变化 */ (StringUtils.equalsIgnoreCase(alarmLevelCur, alarmLevel)) {
                        Integer hasConfirm = produceInfo.getHasConfirm();
                        if /* 已确认 */ (Integer.valueOf(1).equals(hasConfirm)) {
                            // 1.生成最新告警
//                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                            // 2.之前告警转为历史告警
                            produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                            produceInfo.setWithHistory(1);
                            produceInfoService.getBaseMapper().updateById(produceInfo);
                            // 3.状态机制
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    SIGNAL_DATA_ALARM
                            ));
                        } /* 未确认 */ else {
                            // 不生成告警, 直接进入状态机制
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    SIGNAL_DATA_ALARM
                            ));
                        }
                    } /* 告警等级发生变化 */ else {
                        // 1.生成最新告警
//                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                        // 2.之前告警转为历史告警
                        produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                        produceInfo.setWithHistory(1);
                        produceInfoService.getBaseMapper().updateById(produceInfo);
                        // 3.状态机制
                        applicationContext.publishEvent(new StatusDataReceiveEvent(
                                monitorNo,
                                monitorType,
                                eventTimeStamp,
                                SIGNAL_DATA_ALARM
                        ));
                    }

                }
            } /* 不产生告警 */ else {
                if /* 不存在当前告警信息 */ (Objects.isNull(produceInfo)) {
                    // 状态机制
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            SIGNAL_DATA_NO_ALARM
                    ));
                } /* 存在当前告警信息 */ else {
                    // 1.告警恢复机制
                    String chainInfo = produceInfo.getChainInfo();
                    applicationContext.publishEvent(new AlarmRelieveEvent(
                            monitorNo,
                            chainInfo
                    ));
                    // 2.状态机制
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            SIGNAL_DATA_NO_ALARM
                    ));
                }
            }
        } else if (MONITOR_NUM.equals(monitorType)){
            // 遥测数据 告警
            String ruleNo = alarmConfigMonitor.getRuleNo();
            // 获取规则规则详情信息
            AlarmConfigRule alarmConfigRule = obtainRule(ruleNo);
            // 判断告警(遥测数据 -> 告警/一级预警/二级预警/无告警)
            AlarmResultDTO alarmResultDTO = judgeAlarmOccur(monitorValue, alarmConfigRule);
            /* 是否产生了告警(1产生了告警) */
            @NotNull Integer produce = alarmResultDTO.getProduce();
            @NotNull Integer levelProduce = alarmResultDTO.getLevel();
            // 提取状态
            final Integer curStatus = extractNumDataStatus(levelProduce, produce);
            // 查询当前告警信息
            final AlarmProduceInfo produceInfo = produceInfoService.fetchCurAlarmInfoByMonitorNo(monitorNo);
            if /* 产生告警 */ (NUM_ALARM.equals(produce)) {
                if /* 无告警信息 */ (Objects.isNull(produceInfo)) {
                    // 1.生成告警信息并保存
                    LocalDateTime curTime = LocalDateTime.now();
                    String levelCur = fetchAlarmLevelByDataTypeAndLevel(alarmConfigMonitor.getRuleNo(), "num", levelProduce);
                    AlarmProduceInfo produceAlarmInfo = AlarmProduceInfo.builder()
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
                            .chainInfo(UUID.randomUUID().toString())
                            .nextInfoNo(null)
                            .hasConfirm(0)
                            .confirmPerson(null)
                            .confirmTime(null)
                            .hasRestore(0)
                            .restoreTime(null)
                            .alarmDate(curTime)
                            .alarmTimestamp(curTime)
                            .rowStamp(genRow())
                            // todo 告警描述
                            .alarmLevel(levelCur)
                            .build();
                    int insert = produceInfoService.getBaseMapper().insert(produceAlarmInfo);
                    assert insert == 1;
                    // 2.状态机制
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            curStatus
                    ));
                } /* 有告警信息 */ else {

                    //# --------------------获取告警等级--------------------
                    String levelNo = null;
                    switch (levelProduce) {
                        case 1:
                            levelNo = alarmConfigRule.getLevelNoAlarm();
                            break;
                        case 2:
                            levelNo = alarmConfigRule.getLevelNoOne();
                            break;
                        case 3:
                            levelNo = alarmConfigRule.getLevelNoTwo();
                            break;
                        default:
                            // never here
                    }
                    assert levelNo != null;
                    AlarmConfigLevel alarmConfigLevel = obtainAlarmLevelByLevelNo(levelNo);
                    final String alarmLevelCur = alarmConfigLevel.getLevelIllustrate();
                    //# --------------------获取告警等级--------------------

                    // 1.告警等级是否发生变化
                    String alarmLevel = produceInfo.getAlarmLevel();
                    if /* 告警等级没有发生变化 */ (StringUtils.equalsIgnoreCase(alarmLevelCur, alarmLevel)) {
                        final Integer hasConfirm = produceInfo.getHasConfirm();
                        if /* 已确认 */ (Integer.valueOf(1).equals(hasConfirm)) {
                            // 1.生成最新告警
//                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                            // 2.之前告警转为历史告警
                            produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                            produceInfo.setWithHistory(1);
                            produceInfoService.getBaseMapper().updateById(produceInfo);
                            // 3.状态机制
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    curStatus
                            ));
                        } /* 未确认 */ else {
                            // 不生成告警, 直接进入状态机制
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    curStatus
                            ));
                        }
                    } /* 告警等级发生变化 */ else {
                        // 1.生成最新告警
//                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                        // 2.之前告警转为历史告警
                        produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                        produceInfo.setWithHistory(1);
                        produceInfoService.getBaseMapper().updateById(produceInfo);
                        // 3.状态机制
                        applicationContext.publishEvent(new StatusDataReceiveEvent(
                                monitorNo,
                                monitorType,
                                eventTimeStamp,
                                curStatus
                        ));
                    }
                }
            } /* 无告警产生 */ else {
                if /* 无告警信息 */ (Objects.isNull(produceInfo)) {
                    // 状态机制
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            curStatus
                    ));
                } /* 有告警信息 */ else {
                    // 1.告警恢复机制
                    String chainInfo = produceInfo.getChainInfo();
                    applicationContext.publishEvent(new AlarmRelieveEvent(
                            monitorNo,
                            chainInfo
                    ));
                    // 2.状态机制
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            curStatus
                    ));
                }
            }

        }

    }


    // --------------------------------------------------------auxiliary method--------------------------------------------------------


    String fetchAlarmLevelByDataTypeAndLevel(String ruleNo, String type, Integer level) {
        AlarmConfigRule configRule = obtainRule(ruleNo);
        String levelNo = null;
        if ("num".equalsIgnoreCase(type)) {
            assert level != null;
            switch (level) {
                case 1:
                    levelNo = configRule.getLevelNoAlarm();
                    break;
                case 2:
                    levelNo = configRule.getLevelNoOne();
                    break;
                case 3:
                    levelNo = configRule.getLevelNoTwo();
                    break;
                default:
                    // never here
            }
        } else if ("bool".equalsIgnoreCase(type)) {
            levelNo = configRule.getLevelNoAlarm();
        }
        AlarmConfigLevel configLevel = obtainAlarmLevelByLevelNo(levelNo);
        return configLevel.getLevelIllustrate();
    }


    static Long genRow() {
        return SnowflakeArithmeticUtil.nextId();
    }


    static Integer extractNumDataStatus(Integer alarmLevel, Integer produce) {
        Integer curStatus = null;
        if (MONITOR_NUM.equals(produce)) {
            switch (alarmLevel) {
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
//                log.error("提取当前状态时出现异常状态,告警判断结果[{}],测点配置信息:[{}]", alarmResultDTO, alarmConfigMonitor);
//            throw new IllegalStateException("提取当前状态时出现异常状态");
            return null;
        }
        return curStatus;
    }

    // 生成最新的告警信息

    AlarmProduceInfo generateNewestAlarmByPreAlarm(MonitorStructDTO monitorStructDTO, AlarmProduceInfo produceInfo, String alarmLevel) {
        LocalDateTime eventDateTime = Instant.ofEpochMilli(monitorStructDTO.getEventTimeStamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return generateNewestAlarmByPreAlarm(
                alarmLevel,
                monitorStructDTO.getMonitorValue(),
                monitorStructDTO.getMonitorValueStr(),
                eventDateTime,
                produceInfo
        );
    }

    private AlarmProduceInfo generateNewestAlarmByPreAlarm(
            String alarmLevel,
            BigDecimal monitorValue, String monitorValueStr, LocalDateTime eventDateTime,
            AlarmProduceInfo produceInfo) {
        LocalDateTime curTime = LocalDateTime.now();
        AlarmProduceInfo newestAlarmInfo = AlarmProduceInfo.builder()
                .version(0)
                .monitorNo(produceInfo.getMonitorNo())
                .groupType(produceInfo.getGroupType())
                .ruleNo(produceInfo.getRuleNo())
                .eventTime(eventDateTime)
                .processTime(curTime)
                .monitorNum(monitorValue)
                .monitorNumStr(monitorValueStr)
                .createTime(curTime)
                .withHistory(0)
                .preInfoNo(produceInfo.getInfoNo())
                .chainInfo(produceInfo.getChainInfo())
                .nextInfoNo(null)
                .hasConfirm(0)
                .confirmPerson(null)
                .confirmTime(null)
                .hasRestore(0)
                .restoreTime(null)
                .alarmDate(curTime)
                .alarmTimestamp(curTime)
                .rowStamp(genRow())
                // todo 告警描述
                .alarmLevel(alarmLevel)
                .build();
        produceInfoService.getBaseMapper().insert(newestAlarmInfo);
        return newestAlarmInfo;
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
     * 获取告警等级
     *
     * @param levelNo   等级编码
     * @return          告警等级配置
     */
    public AlarmConfigLevel obtainAlarmLevelByLevelNo(String levelNo) {
        AlarmConfigLevel alarmConfigLevel = null;
        String cacheEntry = (String) redisTemplate.opsForHash().get(RedisCacheKeyNameConstants.REDIS_LEVEL, levelNo);
        if (StringUtils.isNotBlank(cacheEntry)) {
            try {
                alarmConfigLevel = objectMapper.readValue(cacheEntry, AlarmConfigLevel.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(alarmConfigLevel)) {
                return alarmConfigLevel;
            }
        }
        QueryWrapper<AlarmConfigLevel> monitorQueryWrapper = new QueryWrapper<>();
        monitorQueryWrapper.eq("level_no", levelNo);
        alarmConfigLevel = levelService.getBaseMapper().selectOne(monitorQueryWrapper);
        // load to cache
        if (Objects.nonNull(alarmConfigLevel)) {
            try {
                String cacheNo = alarmConfigLevel.getLevelNo();
                String storeToRedis = objectMapper.writeValueAsString(alarmConfigLevel);
                redisTemplate.opsForHash().put(RedisCacheKeyNameConstants.REDIS_LEVEL, cacheNo, storeToRedis);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return alarmConfigLevel;
    }


    /**
     * 获取告警状态
     * @param monitorId     测点id
     * @return  告警状态
     */
    public MonitorStatusInfo obtainAlarmStatusByMonitorId(String monitorId) {
        String cacheEntry = (String) redisTemplate.opsForHash().get(RedisCacheKeyNameConstants.REDIS_STATUS, monitorId);
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
     * @return 测点配置
     */
    AlarmConfigMonitor obtainMonitor(String monitorId) {
        AlarmConfigMonitor alarmConfigMonitor = null;
        // read from cache
        String cacheEntry = (String) redisTemplate.opsForHash().get(RedisCacheKeyNameConstants.REDIS_MONITOR, monitorId);
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
        monitorQueryWrapper.eq("monitor_no", monitorId);
        alarmConfigMonitor = configMonitorService.getBaseMapper().selectOne(monitorQueryWrapper);
        // load to cache
        if (Objects.nonNull(alarmConfigMonitor)) {
            try {
                String monitorNo = alarmConfigMonitor.getMonitorNo();
                String storeToRedis = objectMapper.writeValueAsString(alarmConfigMonitor);
                redisTemplate.opsForHash().put(RedisCacheKeyNameConstants.REDIS_MONITOR, monitorNo, storeToRedis);
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
        final String ruleEntry = (String) redisTemplate.opsForHash().get(RedisCacheKeyNameConstants.REDIS_RULE, ruleNo);
        if (Objects.nonNull(ruleEntry)) {
            try {
                alarmConfigRule = objectMapper.readValue(ruleEntry, AlarmConfigRule.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (Objects.isNull(alarmConfigRule)) {
            QueryWrapper<AlarmConfigRule> ruleQueryWrapper = new QueryWrapper<>();
            ruleQueryWrapper.eq("rule_no", ruleNo);
            alarmConfigRule = configRuleService.getBaseMapper().selectOne(ruleQueryWrapper);
            // load to cache
            if (Objects.nonNull(alarmConfigRule)) {
                try {
                    String perRuleNo = alarmConfigRule.getRuleNo();
                    String ruleStr = objectMapper.writeValueAsString(alarmConfigRule);
                    redisTemplate.opsForHash().put(RedisCacheKeyNameConstants.REDIS_RULE, perRuleNo, ruleStr);
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
