package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.alarm.domain.convert.AlarmConfigMonitorToDtoConvert;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigMonitor;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigMonitorMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmmonitor.AlarmMonitorSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigMonitorService;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 测点 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-07-05
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigMonitorServiceImpl extends ServiceImpl<AlarmConfigMonitorMapper, AlarmConfigMonitor> implements IAlarmConfigMonitorService {


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override public void updateRuleNoByIds(String ruleNo, Set<String> monitorNos) {
        if (StringUtils.isBlank(ruleNo)) {
            throw new IllegalArgumentException("规则编码必须");
        }
        if (CollectionUtils.isEmpty(monitorNos)) {
            throw new IllegalArgumentException("待更新的监测点编码不能为空");
        }
        // 查询
        QueryWrapper<AlarmConfigMonitor> monitorQueryWrapper = new QueryWrapper<>();
        monitorQueryWrapper.in("monitor_no", monitorNos);
        List<AlarmConfigMonitor> alarmConfigMonitors = getBaseMapper().selectList(monitorQueryWrapper);
        // 校验测点数据
        if (CollectionUtils.isEmpty(alarmConfigMonitors)) {
            throw new IllegalStateException("没有查询到对应的测点信息");
        }
        if (alarmConfigMonitors.size() != monitorNos.size()) {
            Set<String> monitorSet = alarmConfigMonitors.stream().map(AlarmConfigMonitor::getMonitorNo).collect(Collectors.toSet());
            String noSuchData = monitorNos.stream().filter(p -> !monitorSet.contains(p)).collect(Collectors.joining(",", "(", ")"));
            throw new IllegalStateException("没有获取到测点" + noSuchData + "对应的信息");
        }
        List<String> hasRule = alarmConfigMonitors.stream().filter(p -> StringUtils.isNotBlank(p.getRuleNo())).map(AlarmConfigMonitor::getMonitorNo).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(hasRule)) {
            String tmpStr = hasRule.stream().collect(Collectors.joining(",", "(", ")"));
            throw new IllegalStateException("测点" + tmpStr + "已含有对应的规则");
        }
        // 更新数据
        final String fnlRuleNo = StringUtils.trim(ruleNo);
        alarmConfigMonitors.forEach(item -> item.setRuleNo(fnlRuleNo));
        updateBatchById(alarmConfigMonitors);

    }

    @Override
    public Pager<AlarmMonitorDTO> queryByPage(AlarmMonitorSimpleQuery query) {
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("查询参数不能为空.");
        }
        Page<AlarmConfigMonitor> page = new Page<>(query.getPageNumber(), query.getPageSize());
        QueryWrapper<AlarmConfigMonitor> criteria = new QueryWrapper<>();
        criteria.isNull("rule_no");
        if (StringUtils.isNotBlank(query.getMonitorDescribe())) {
            criteria.like("monitor_describe", StringUtils.trim(query.getMonitorDescribe()));
        }
        Page<AlarmConfigMonitor> pageResults = getBaseMapper().selectPage(page, criteria);
        List<AlarmConfigMonitor> records = pageResults.getRecords();
        // 转换结果
        List<AlarmMonitorDTO> dtoList = AlarmConfigMonitorToDtoConvert.INSTANCE.to(records);
        return new Pager<>((int)pageResults.getTotal(), dtoList);
    }

    @Override public Map<String, List<AlarmMonitorDTO>> queryMonitorMapByRuleNos(List<String> ruleNos) {
        QueryWrapper<AlarmConfigMonitor> monitorQueryWrapper = new QueryWrapper<>();
        monitorQueryWrapper.in("rule_no", ruleNos);
        List<AlarmConfigMonitor> alarmConfigMonitors = getBaseMapper().selectList(monitorQueryWrapper);
        List<AlarmMonitorDTO> monitorDTOList = AlarmConfigMonitorToDtoConvert.INSTANCE.to(alarmConfigMonitors);
        return monitorDTOList.stream().filter(Objects::nonNull).filter(p -> StringUtils.isNotBlank(p.getRuleNo()))
                .collect(Collectors.groupingBy(AlarmMonitorDTO::getRuleNo));
    }

    @Override public void disconnectMonitorWithRule(String ruleNo) {
        UpdateWrapper<AlarmConfigMonitor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("rule_no = null");
        updateWrapper.eq("rule_no", StringUtils.trim(ruleNo));
        getBaseMapper().update(null, updateWrapper);
    }

}
