package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.alarm.domain.convert.AlarmConfigLevelToDtoConvert;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigLevel;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigLevelMapper;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigRuleMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AddNewAlarmLevelQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AlarmLevelSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 告警等级表 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigLevelServiceImpl extends ServiceImpl<AlarmConfigLevelMapper, AlarmConfigLevel> implements IAlarmConfigLevelService {

    private final AlarmConfigRuleMapper ruleMapper;

    @Override public boolean addNewAlarmLevel(AddNewAlarmLevelQuery query) {
        if (Objects.isNull(query)) {
            return false;
        }
        final String uuid = UUID.randomUUID().toString();
        // 转换
        AlarmConfigLevel configLevel = AlarmConfigLevel.builder()
                .levelNo(uuid)
                .levelIllustrate(query.getAlarmLevelName())
                .alarmWay(query.getAlarmWay())
                .mark(query.getMark())
                .build();
        int insert = getBaseMapper().insert(configLevel);
        assert insert == 1;
        return true;
    }

    @Override public Pager<AlarmLevelDTO> queryAlarmLevel(AlarmLevelSimpleQuery query) {
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("查询参数不能为空.");
        }
        Page<AlarmConfigLevel> page = new Page<>(query.getPageNumber(), query.getPageSize());
        QueryWrapper<AlarmConfigLevel> criteria = new QueryWrapper<>();
        Page<AlarmConfigLevel> pageResults = getBaseMapper().selectPage(page, criteria);
        List<AlarmConfigLevel> records = pageResults.getRecords();
        // 转换结果
        List<AlarmLevelDTO> dtoList = AlarmConfigLevelToDtoConvert.INSTANCE.to(records);
        return new Pager<>(dtoList);
    }

    @Override public AlarmLevelDTO deleteByLevelNo(String levelNo) {
        // 查询规则表, 看是否存在关联数据, 如果存在则无法删除
        QueryWrapper<AlarmConfigRule> ruleQueryWrapper = new QueryWrapper<>();
        ruleQueryWrapper.eq("level_no_alarm", levelNo)
                .or()
                .eq("level_no_one", levelNo)
                .or()
                .eq("level_no_two", levelNo);
        Integer integer = ruleMapper.selectCount(ruleQueryWrapper);
        if (Objects.nonNull(integer) && integer > 0) {
            throw new IllegalStateException("规则数据中存在对此数据的关联, 无法删除.");
        }
        // 查询数据 - 校验数据是否存在
        AlarmConfigLevel configLevel = getBaseMapper().selectOne(
                new QueryWrapper<AlarmConfigLevel>().eq("", "")
        );
        if (Objects.isNull(configLevel)) {
            throw new IllegalStateException("该条数据不存在, 非法状态.");
        }
        // 删除数据
        getBaseMapper().deleteById(configLevel.getId());
        return AlarmConfigLevelToDtoConvert.INSTANCE.to(configLevel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override public List<AlarmLevelDTO> batchDelete(List<String> levelNos) {
        if (CollectionUtils.isEmpty(levelNos)) {
            throw new IllegalArgumentException("参数数据为空.");
        }
        final List<AlarmLevelDTO> results = new ArrayList<>(levelNos.size());
        for (String levelNo : levelNos) {
            AlarmLevelDTO alarmLevelDTO = deleteByLevelNo(levelNo);
            results.add(alarmLevelDTO);
        }
        return results;
    }


}
