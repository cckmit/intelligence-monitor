package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.alarm.domain.convert.AlarmConfigRuleToDtoConvert;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.InnerAlarmRuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigRuleMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AddNewAlarmRuleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AlarmRuleSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigLevelService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 规则表 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Slf4j
@Service
public class AlarmConfigRuleServiceImpl extends ServiceImpl<AlarmConfigRuleMapper, AlarmConfigRule> implements IAlarmConfigRuleService {

    private final IAlarmConfigMonitorService monitorService;

    private final IAlarmConfigLevelService levelService;

    private final IAlarmConfigRuleService configRuleService;

    public AlarmConfigRuleServiceImpl(IAlarmConfigMonitorService monitorService,
                                      // 解决循环依赖
                                      @Lazy IAlarmConfigLevelService levelService,
                                      @Lazy IAlarmConfigRuleService configRuleService) {
        this.monitorService = monitorService;
        this.levelService = levelService;
        this.configRuleService = configRuleService;
    }

    @Override public Integer queryCountByLevelNo(String levelNo) {
        QueryWrapper<AlarmConfigRule> ruleQueryWrapper = new QueryWrapper<>();
        ruleQueryWrapper.eq("level_no_alarm", levelNo)
                .or()
                .eq("level_no_one", levelNo)
                .or()
                .eq("level_no_two", levelNo);
        ruleQueryWrapper.eq("delete_mark", 0);
        return getBaseMapper().selectCount(ruleQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override public boolean addNewAlarmRule(AddNewAlarmRuleQuery query) {
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        Integer ruleType = query.getRuleType();
        if (Objects.isNull(query.getRuleType()) || Objects.isNull(query.getGroupType())) {
            throw new IllegalArgumentException("规则类型和分组类型必传");
        }
        if (ruleType == 0) {
            // 遥信数据
            if (StringUtils.isBlank(query.getAlarmRuleName())
                    || CollectionUtils.isEmpty(query.getMonitors())
                    || StringUtils.isBlank(query.getAlarmType())
                    || StringUtils.isBlank(query.getAlarmRegionConnLevel()) || Objects.isNull(query.getRuleType())) {
                throw new IllegalArgumentException("(告警策略名称,关联测点,告警类型,告警区间-关联告警等级,规则类型)必传且不能为空");
            }
            // 校验该名称是否存在
            checkRuleNameExistOrNotThenThrowExc(StringUtils.trim(query.getAlarmRuleName()));
            // 存储
            final LocalDateTime createTime = LocalDateTime.now();
            AlarmConfigRule alarmConfigRule = AlarmConfigRule.builder()
                    .version(0)
                    .alarmRuleName(query.getAlarmRuleName())
                    .alarmType(query.getAlarmType())
                    .levelNoAlarm(query.getAlarmRegionConnLevel())
                    .createTime(createTime)
                    .updateTime(createTime)
                    .deleteMark(0)
                    .ruleType(query.getRuleType())
                    .groupType(query.getGroupType())
                    .build();
            getBaseMapper().insert(alarmConfigRule);
            final String ruleNo = alarmConfigRule.getRuleNo();
            assert StringUtils.isNotBlank(ruleNo);
            // 更新测点关联的规则值
            monitorService.updateRuleNoByIds(ruleNo, new HashSet<>(query.getMonitors()));
        } else if (ruleType == 1) {
            // 遥测数据
            /*
                告警策略名称
                关联测点
                告警类型
                告警区间
                告警区间-关联告警等级
             */
            if (StringUtils.isBlank(query.getAlarmRuleName())
                    || CollectionUtils.isEmpty(query.getMonitors())
                    || StringUtils.isBlank(query.getAlarmType())
                    || StringUtils.isBlank(query.getAlarmRegion())
                    || StringUtils.isBlank(query.getAlarmRegionConnLevel()) || Objects.isNull(query.getRuleType())) {
                throw new IllegalArgumentException("(告警策略名称,关联测点,告警类型,告警区间,告警区间-关联告警等级,规则类型)必传且不能为空");
            }
            // 校验该名称是否存在
            checkRuleNameExistOrNotThenThrowExc(StringUtils.trim(query.getAlarmRuleName()));
            // 存储
            final LocalDateTime createTime = LocalDateTime.now();
            AlarmConfigRule alarmConfigRule = AlarmConfigRule.builder()
                    .version(0)
                    .alarmRuleName(query.getAlarmRuleName())
                    .alarmType(query.getAlarmType())
                    .alarmValue(query.getAlarmRegion())
                    .levelNoAlarm(query.getAlarmRegionConnLevel())
                    .preAlarmOneValue(query.getPreAlarmOne())
                    .levelNoOne(query.getPreAlarmOneConnLevel())
                    .preAlarmTwoValue(query.getPreAlarmTwo())
                    .levelNoTwo(query.getPreAlarmTwoConnLevel())
                    .createTime(createTime)
                    .updateTime(createTime)
                    .deleteMark(0)
                    .ruleType(query.getRuleType())
                    .groupType(query.getGroupType())
                    .build();
            getBaseMapper().insert(alarmConfigRule);
            final String ruleNo = alarmConfigRule.getRuleNo();
            assert StringUtils.isNotBlank(ruleNo);
            // 更新测点关联的规则值
            monitorService.updateRuleNoByIds(ruleNo, new HashSet<>(query.getMonitors()));
        } else {
            throw new IllegalArgumentException("不能识别的规则类型");
        }
        return true;
    }

    private void checkRuleNameExistOrNotThenThrowExc(String alarmRuleName) {
        Integer integer = getBaseMapper().selectCount(
                new QueryWrapper<AlarmConfigRule>()
                .eq("alarm_rule_name", alarmRuleName)
                .eq("delete_mark", 0)
        );
        if (Objects.nonNull(integer) && integer > 0) {
            throw new IllegalStateException("该策略名称[" + alarmRuleName + "]已存在.");
        }
    }

    @Override public Pager<InnerAlarmRuleDTO> queryByPage(AlarmRuleSimpleQuery query) {
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("查询参数不能为空");
        }
        if (Objects.isNull(query.getGroupType())) {
            throw new IllegalArgumentException("分组类型必须");
        }
        Page<AlarmConfigRule> pageCriteria = new Page<>(query.getPageNumber(), query.getPageSize());
        QueryWrapper<AlarmConfigRule> queryCriteria = new QueryWrapper<>();
        queryCriteria.ne("delete_mark", 1);
        queryCriteria.eq("group_type", query.getGroupType());
        queryCriteria.eq("rule_type", query.getRuleType());
        Page<AlarmConfigRule> pageResult = getBaseMapper().selectPage(pageCriteria, queryCriteria);
        List<AlarmConfigRule> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new Pager<>((int) pageResult.getTotal(), Collections.emptyList());
        }
        List<InnerAlarmRuleDTO> dtoList = AlarmConfigRuleToDtoConvert.INSTANCE.to(records);
        // 查询测点信息
        List<String> ruleNos = records.stream().filter(Objects::nonNull).map(AlarmConfigRule::getRuleNo).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ruleNos)) {
            final Map<String, List<AlarmMonitorDTO>> monitorGroup = monitorService.queryMonitorMapByRuleNos(ruleNos);
            dtoList.forEach(item -> item.setWithMonitors(monitorGroup.get(item.getRuleNo())));
        }
        // 查询等级信息
        final Map<String, AlarmLevelDTO> levelDTOMap = levelService.queryLevelMapAll();
        dtoList.forEach(item -> {
            item.setLevelNoAlarmInfo(levelDTOMap.get(item.getLevelNoAlarm()));
            item.setLevelNoOneInfo(levelDTOMap.get(item.getLevelNoOne()));
            item.setLevelNoTwoInfo(levelDTOMap.get(item.getLevelNoTwo()));
        });
        return new Pager<>((int) pageResult.getTotal(), dtoList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override public InnerAlarmRuleDTO changeRule(InnerAlarmRuleDTO query) {
        log.info("changeRule入参[{}]", query);
        /*
            1.更新当前rule数据
            2.断开测点关系
            3.关联测点关系
         */
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("参数不能为空");
        }

        Integer ruleType = query.getRuleType();
        if (Objects.isNull(ruleType)) {
            throw new IllegalArgumentException("规则类型必传");
        }
        if (ruleType == 0) {
            // 遥信数据
            final Set<String> monitorNos = query.getWithMonitors().stream().filter(Objects::nonNull).map(AlarmMonitorDTO::getMonitorNo).filter(Objects::nonNull).collect(Collectors.toSet());
            if (StringUtils.isBlank(query.getRuleNo())
                    || StringUtils.isBlank(query.getAlarmRuleName())
                    || StringUtils.isBlank(query.getAlarmType())
                    || CollectionUtils.isEmpty(query.getWithMonitors()) || CollectionUtils.isEmpty(monitorNos)
                    || Objects.isNull(query.getLevelNoAlarmInfo())) {
                throw new IllegalArgumentException("(规则编码,规则名称,告警类型,告警区间-关联告警等级)必传且不能为空");
            }
            // 根据规则编码查询
            QueryWrapper<AlarmConfigRule> ruleQueryWrapper = new QueryWrapper<>();
            ruleQueryWrapper.eq("rule_no", StringUtils.trim(query.getRuleNo()));
            AlarmConfigRule alarmConfigRule = getBaseMapper().selectOne(ruleQueryWrapper);
            if (Objects.isNull(alarmConfigRule)) {
                throw new IllegalStateException(String.format("非法状态,规则编码[%s]对应的数据不存在", query.getRuleNo()));
            }
            // 检验规则名称是否已存在 - 如果修改了规则名称
            if (!StringUtils.equalsIgnoreCase(alarmConfigRule.getAlarmRuleName(), query.getAlarmRuleName())) {
                checkRuleNameExistOrNotThenThrowExc(query.getAlarmRuleName());
                // 修改规则名称
                alarmConfigRule.setAlarmRuleName(query.getAlarmRuleName());
            }
            /* 更新rule信息 */
            alarmConfigRule.setAlarmType(query.getAlarmType());
            alarmConfigRule.setLevelNoAlarm(query.getLevelNoAlarmInfo().getLevelNo());

            getBaseMapper().updateById(alarmConfigRule);
            /* 断开测点关系 */
            monitorService.disconnectMonitorWithRule(alarmConfigRule.getRuleNo());
            /* 关联测点关系 */
            monitorService.updateRuleNoByIds(alarmConfigRule.getRuleNo(), monitorNos);
        } else if (ruleType == 1) {
            // 遥测数据
            final Set<String> monitorNos = query.getWithMonitors().stream().filter(Objects::nonNull).map(AlarmMonitorDTO::getMonitorNo).filter(Objects::nonNull).collect(Collectors.toSet());
            if (StringUtils.isBlank(query.getRuleNo())
                    || StringUtils.isBlank(query.getAlarmRuleName())
                    || StringUtils.isBlank(query.getAlarmType())
                    || CollectionUtils.isEmpty(query.getWithMonitors()) || CollectionUtils.isEmpty(monitorNos)
                    || StringUtils.isBlank(query.getAlarmValue())
                    || Objects.isNull(query.getLevelNoAlarmInfo())) {
                throw new IllegalArgumentException("(规则编码,规则名称,告警类型,告警区间,告警区间-关联告警等级)必传且不能为空");
            }
            // 根据规则编码查询
            QueryWrapper<AlarmConfigRule> ruleQueryWrapper = new QueryWrapper<>();
            ruleQueryWrapper.eq("rule_no", StringUtils.trim(query.getRuleNo()));
            AlarmConfigRule alarmConfigRule = getBaseMapper().selectOne(ruleQueryWrapper);
            if (Objects.isNull(alarmConfigRule)) {
                throw new IllegalStateException(String.format("非法状态,规则编码[%s]对应的数据不存在", query.getRuleNo()));
            }
            // 检验规则名称是否已存在 - 如果修改了规则名称
            if (!StringUtils.equalsIgnoreCase(alarmConfigRule.getAlarmRuleName(), query.getAlarmRuleName())) {
                checkRuleNameExistOrNotThenThrowExc(query.getAlarmRuleName());
                // 修改规则名称
                alarmConfigRule.setAlarmRuleName(query.getAlarmRuleName());
            }
            /* 更新rule信息 */
            alarmConfigRule.setAlarmType(query.getAlarmType());
            // 告警
            alarmConfigRule.setAlarmValue(query.getAlarmValue());
            alarmConfigRule.setLevelNoAlarm(query.getLevelNoAlarmInfo().getLevelNo());
            // 一级预警
            alarmConfigRule.setPreAlarmOneValue(query.getPreAlarmOneValue());
            alarmConfigRule.setLevelNoOne(query.getLevelNoOneInfo().getLevelNo());
            // 二级预警
            alarmConfigRule.setPreAlarmTwoValue(query.getPreAlarmTwoValue());
            alarmConfigRule.setLevelNoTwo(query.getLevelNoTwoInfo().getLevelNo());

            getBaseMapper().updateById(alarmConfigRule);
            /* 断开测点关系 */
            monitorService.disconnectMonitorWithRule(alarmConfigRule.getRuleNo());
            /* 关联测点关系 */
            monitorService.updateRuleNoByIds(alarmConfigRule.getRuleNo(), monitorNos);
        } else {
            throw new IllegalArgumentException("不能识别的规则类型");
        }
        return query;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override public boolean deleteRule(String ruleNo) {
        if (StringUtils.isBlank(ruleNo)) {
            throw new IllegalArgumentException("规则编码不能为空.");
        }
        QueryWrapper<AlarmConfigRule> configRuleQueryWrapper = new QueryWrapper<>();
        configRuleQueryWrapper.eq("rule_no", StringUtils.trim(ruleNo));
        AlarmConfigRule alarmConfigRule = getBaseMapper().selectOne(configRuleQueryWrapper);
        if (Objects.isNull(alarmConfigRule)) {
            throw new IllegalStateException(String.format("规则编码[%s]对应的数据不存在", StringUtils.trim(ruleNo)));
        }
        if (Integer.valueOf(1).equals(alarmConfigRule.getDeleteMark())) {
            throw new IllegalStateException(String.format("规则编码[%s]对应的数据已删除", StringUtils.trim(ruleNo)));
        }
        alarmConfigRule.setDeleteMark(1);
        int i = getBaseMapper().updateById(alarmConfigRule);
        assert i == 1;
        // 断开测点与规则的关系
        monitorService.disconnectMonitorWithRule(ruleNo);
        // todo 对应规则已删除, 需要删除缓存
        return true;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override public boolean batchDelete(List<String> ruleNos) {
        if (CollectionUtils.isEmpty(ruleNos)) {
            throw new IllegalArgumentException("告警规则为空.");
        }
        for (String ruleNo : ruleNos) {
            configRuleService.deleteRule(ruleNo);
        }
        return true;
    }

}
