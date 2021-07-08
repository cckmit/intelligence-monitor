package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmRuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigRuleMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AddNewAlarmRuleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AlarmRuleSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;

/**
 * <p>
 * 规则表 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigRuleServiceImpl extends ServiceImpl<AlarmConfigRuleMapper, AlarmConfigRule> implements IAlarmConfigRuleService {

    private final IAlarmConfigMonitorService monitorService;

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
        final String alarmRuleName = StringUtils.trim(query.getAlarmRuleName());
        // 校验该名称是否存在
        Integer integer = getBaseMapper().selectCount(new QueryWrapper<AlarmConfigRule>().eq("alarm_rule_name", alarmRuleName));
        if (Objects.nonNull(integer) && integer > 0) {
            throw new IllegalStateException("该策略名称[" + alarmRuleName + "]已存在.");
        }
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
                .build();
        final String ruleNo = alarmConfigRule.getRuleNo();
        assert StringUtils.isNotBlank(ruleNo);
        // 更新测点关联的规则值
        monitorService.updateRuleNoByIds(ruleNo, new HashSet<>(query.getMonitors()));
        return true;
    }

    @Override public Pager<AlarmRuleDTO> queryByPage(AlarmRuleSimpleQuery query) {
        // todo

        return new Pager<>(null);
    }

}
