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
 * ???????????? -> ????????????
 * <p>
 *     ????????????: (???????????????????????????????????????????????????)
 *     1.?????????????????? -> ????????????
 *     2.?????????????????????, ??????????????????????????????
 *          2.1 ??????????????? -> ???????????????
 *          2.2 ??????????????? -> ??????????????????
 *     3.?????????????????????, ???????????????????????????  -> ????????????, ??????????????????????????????
 * </p>
 *
 * <p>
 *     ???????????????(??????->????????????)
 *      ????????????
 *          1.???????????????????????????????????????
 *          2.?????????????????????????????????????????????????????????????????????
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
     * ????????????
     */
    public static final Integer MONITOR_BOOLEAN = 0;

    /**
     * ????????????
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
            log.error("??????????????????,????????????????????????null");
            return;
        }
        final String monitorNo = monitorStructDTO.getMonitorNo();
        final BigDecimal monitorValue = monitorStructDTO.getMonitorValue();
        final String monitorValueStr = monitorStructDTO.getMonitorValueStr();
        final Long eventTimeStamp = monitorStructDTO.getEventTimeStamp();
        if (Objects.isNull(monitorNo)
                || Objects.isNull(monitorValue) || Objects.isNull(monitorValueStr)
                || Objects.isNull(eventTimeStamp)) {
            log.error("??????(????????????/?????????/???????????????)???????????????,????????????:[{}]", monitorStructDTO);
            return;
        }
        LocalDateTime eventDateTime = Instant.ofEpochMilli(eventTimeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();

        //#
        /*
            2. ??????????????????????????????????????????
                2.1 ???????????????????????????????????????????????????????????? -> ?????????????????????

                ????????????
                ????????????
         */

        // ?????????????????????????????????
        AlarmConfigMonitor alarmConfigMonitor = obtainMonitor(monitorNo);

        Integer monitorType = alarmConfigMonitor.getMonitorType();
        if (MONITOR_BOOLEAN.equals(monitorType)) {
            // ???????????? ?????? -> 1?????? 0?????????
            boolean alarmOccur = BOOLEAN_ALARM.equals(monitorValue);
            // ????????????????????????
            AlarmProduceInfo produceInfo = produceInfoService.fetchCurAlarmInfoByMonitorNo(monitorNo);
            if /* ???????????? */ (alarmOccur) {
                if /* ??????????????????????????? */ (Objects.isNull(produceInfo)) {
                    // 1.???????????????????????????
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
                            // todo ????????????
                            .alarmLevel(levelCur)
                            .build();
                    int insert = produceInfoService.getBaseMapper().insert(produceAlarmInfo);
                    assert insert == 1;
                    // 2.????????????
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            SIGNAL_DATA_ALARM
                    ));
                } /* ???????????????????????? */ else {
                    //# --------------------??????????????????--------------------
                    // ??????????????????
                    String ruleNo = produceInfo.getRuleNo();
                    AlarmConfigRule configRule = obtainRule(ruleNo);
                    String levelNoForAlarm = configRule.getLevelNoAlarm();
                    // ??????????????????
                    AlarmConfigLevel alarmConfigLevel = obtainAlarmLevelByLevelNo(levelNoForAlarm);
                    final String alarmLevelCur = alarmConfigLevel.getLevelIllustrate();
                    //# --------------------??????????????????--------------------

                    // 1.??????????????????????????????(?????????????????????????????????)
                    String alarmLevel = produceInfo.getAlarmLevel();
                    if /* ?????????????????????????????? */ (StringUtils.equalsIgnoreCase(alarmLevelCur, alarmLevel)) {
                        Integer hasConfirm = produceInfo.getHasConfirm();
                        if /* ????????? */ (Integer.valueOf(1).equals(hasConfirm)) {
                            // 1.??????????????????
//                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                            // 2.??????????????????????????????
                            produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                            produceInfo.setWithHistory(1);
                            produceInfoService.getBaseMapper().updateById(produceInfo);
                            // 3.????????????
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    SIGNAL_DATA_ALARM
                            ));
                        } /* ????????? */ else {
                            // ???????????????, ????????????????????????
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    SIGNAL_DATA_ALARM
                            ));
                        }
                    } /* ???????????????????????? */ else {
                        // 1.??????????????????
//                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                        // 2.??????????????????????????????
                        produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                        produceInfo.setWithHistory(1);
                        produceInfoService.getBaseMapper().updateById(produceInfo);
                        // 3.????????????
                        applicationContext.publishEvent(new StatusDataReceiveEvent(
                                monitorNo,
                                monitorType,
                                eventTimeStamp,
                                SIGNAL_DATA_ALARM
                        ));
                    }

                }
            } /* ??????????????? */ else {
                if /* ??????????????????????????? */ (Objects.isNull(produceInfo)) {
                    // ????????????
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            SIGNAL_DATA_NO_ALARM
                    ));
                } /* ???????????????????????? */ else {
                    // 1.??????????????????
                    String chainInfo = produceInfo.getChainInfo();
                    applicationContext.publishEvent(new AlarmRelieveEvent(
                            monitorNo,
                            chainInfo
                    ));
                    // 2.????????????
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            SIGNAL_DATA_NO_ALARM
                    ));
                }
            }
        } else if (MONITOR_NUM.equals(monitorType)){
            // ???????????? ??????
            String ruleNo = alarmConfigMonitor.getRuleNo();
            // ??????????????????????????????
            AlarmConfigRule alarmConfigRule = obtainRule(ruleNo);
            // ????????????(???????????? -> ??????/????????????/????????????/?????????)
            AlarmResultDTO alarmResultDTO = judgeAlarmOccur(monitorValue, alarmConfigRule);
            /* ?????????????????????(1???????????????) */
            @NotNull Integer produce = alarmResultDTO.getProduce();
            @NotNull Integer levelProduce = alarmResultDTO.getLevel();
            // ????????????
            final Integer curStatus = extractNumDataStatus(levelProduce, produce);
            // ????????????????????????
            final AlarmProduceInfo produceInfo = produceInfoService.fetchCurAlarmInfoByMonitorNo(monitorNo);
            if /* ???????????? */ (NUM_ALARM.equals(produce)) {
                if /* ??????????????? */ (Objects.isNull(produceInfo)) {
                    // 1.???????????????????????????
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
                            // todo ????????????
                            .alarmLevel(levelCur)
                            .build();
                    int insert = produceInfoService.getBaseMapper().insert(produceAlarmInfo);
                    assert insert == 1;
                    // 2.????????????
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            curStatus
                    ));
                } /* ??????????????? */ else {

                    //# --------------------??????????????????--------------------
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
                    //# --------------------??????????????????--------------------

                    // 1.??????????????????????????????
                    String alarmLevel = produceInfo.getAlarmLevel();
                    if /* ?????????????????????????????? */ (StringUtils.equalsIgnoreCase(alarmLevelCur, alarmLevel)) {
                        final Integer hasConfirm = produceInfo.getHasConfirm();
                        if /* ????????? */ (Integer.valueOf(1).equals(hasConfirm)) {
                            // 1.??????????????????
//                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                            AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                            // 2.??????????????????????????????
                            produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                            produceInfo.setWithHistory(1);
                            produceInfoService.getBaseMapper().updateById(produceInfo);
                            // 3.????????????
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    curStatus
                            ));
                        } /* ????????? */ else {
                            // ???????????????, ????????????????????????
                            applicationContext.publishEvent(new StatusDataReceiveEvent(
                                    monitorNo,
                                    monitorType,
                                    eventTimeStamp,
                                    curStatus
                            ));
                        }
                    } /* ???????????????????????? */ else {
                        // 1.??????????????????
//                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorValue, monitorValueStr, eventDateTime, produceInfo);
                        AlarmProduceInfo newestAlarmInfo = generateNewestAlarmByPreAlarm(monitorStructDTO, produceInfo, alarmLevel);
                        // 2.??????????????????????????????
                        produceInfo.setNextInfoNo(newestAlarmInfo.getInfoNo());
                        produceInfo.setWithHistory(1);
                        produceInfoService.getBaseMapper().updateById(produceInfo);
                        // 3.????????????
                        applicationContext.publishEvent(new StatusDataReceiveEvent(
                                monitorNo,
                                monitorType,
                                eventTimeStamp,
                                curStatus
                        ));
                    }
                }
            } /* ??????????????? */ else {
                if /* ??????????????? */ (Objects.isNull(produceInfo)) {
                    // ????????????
                    applicationContext.publishEvent(new StatusDataReceiveEvent(
                            monitorNo,
                            monitorType,
                            eventTimeStamp,
                            curStatus
                    ));
                } /* ??????????????? */ else {
                    // 1.??????????????????
                    String chainInfo = produceInfo.getChainInfo();
                    applicationContext.publishEvent(new AlarmRelieveEvent(
                            monitorNo,
                            chainInfo
                    ));
                    // 2.????????????
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
            // ??????
//                log.error("???????????????????????????????????????,??????????????????[{}],??????????????????:[{}]", alarmResultDTO, alarmConfigMonitor);
//            throw new IllegalStateException("???????????????????????????????????????");
            return null;
        }
        return curStatus;
    }

    // ???????????????????????????

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
                // todo ????????????
                .alarmLevel(alarmLevel)
                .build();
        produceInfoService.getBaseMapper().insert(newestAlarmInfo);
        return newestAlarmInfo;
    }

    /**
     * kafka?????????????????????
     * @return  ????????????
     */
    MonitorStructDTO obtainRawMonitorData() {
        // todo

        return null;
    }

    /**
     * ??????????????????
     *
     * @param levelNo   ????????????
     * @return          ??????????????????
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
     * ??????????????????
     * @param monitorId     ??????id
     * @return  ????????????
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
     * ??????????????????
     * @param monitorId ??????id
     * @return ????????????
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
     * ??????????????????
     * @param ruleNo    ????????????
     * @return
     */
    AlarmConfigRule obtainRule(/* ???????????? */ String ruleNo) {
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
     * @param monitorNum        ??????
     * @param alarmConfigRule   ????????????
     * @return  ????????????
     */
    AlarmResultDTO judgeAlarmOccur(BigDecimal monitorNum, AlarmConfigRule alarmConfigRule) {

        AlarmRuleDTO alarmRuleDTO = AlarmRuleDTO.builder()
                .alarmRange(alarmConfigRule.getAlarmValue())
                .preWarningRangeLevelOne(alarmConfigRule.getPreAlarmOneValue())
                .preWarningRangeLevelTwe(alarmConfigRule.getPreAlarmTwoValue())
                .build();

        String uuidMark = UUID.randomUUID().toString();
        log.info("[{}]??????????????????,??????: num[{}], rule[{}]", uuidMark, monitorNum, alarmRuleDTO);
        AlarmResultDTO process = JudgeRuleWithAlarmUtil.process(monitorNum, alarmRuleDTO);
        log.info("[{}]????????????????????????,result:[{}]", uuidMark, process);
        return process;
    }

}
