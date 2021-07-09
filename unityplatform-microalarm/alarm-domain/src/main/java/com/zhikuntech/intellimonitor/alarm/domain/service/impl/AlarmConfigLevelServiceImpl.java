package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.alarm.domain.convert.AlarmConfigLevelToDtoConvert;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigLevel;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigLevelMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AddNewAlarmLevelQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AlarmLevelSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigLevelService;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private final IAlarmConfigRuleService ruleService;

    @Override public boolean addNewAlarmLevel(AddNewAlarmLevelQuery query) {
        if (Objects.isNull(query)) {
            return false;
        }
        // 转换
        AlarmConfigLevel configLevel = AlarmConfigLevel.builder()
                .alarmLevel(query.getAlarmLevel())
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
        return new Pager<>((int) pageResults.getTotal(), dtoList);
    }

    @Override public AlarmLevelDTO deleteByLevelNo(String levelNo) {
        // 查询规则表, 看是否存在关联数据, 如果存在则无法删除
        Integer integer = ruleService.queryCountByLevelNo(levelNo);
        if (Objects.nonNull(integer) && integer > 0) {
            throw new IllegalStateException("规则数据中存在对此数据的关联, 无法删除.");
        }
        // 查询数据 - 校验数据是否存在
        AlarmConfigLevel configLevel = getBaseMapper().selectOne(
                new QueryWrapper<AlarmConfigLevel>().eq("level_no", levelNo)
        );
        if (Objects.isNull(configLevel)) {
            throw new IllegalStateException("该条数据不存在, 非法状态.");
        }
        // 删除数据
        getBaseMapper().deleteById(configLevel.getLevelNo());
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

    @Override public AlarmLevelDTO updateById(AlarmLevelDTO dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (Objects.isNull(dto.getLevelNo())) {
            throw new IllegalArgumentException("标识符编码不能为空");
        }
        // 判断规则中是否有等级关联
        Integer integer = ruleService.queryCountByLevelNo(dto.getLevelNo());
        if (Objects.nonNull(integer) && integer > 0) {
            throw new IllegalStateException("规则数据中存在对此数据的关联, 无法更新.");
        }
        // TODO 判断告警信息中是否有等级关联

        // 更新
        AlarmConfigLevel level = AlarmConfigLevelToDtoConvert.INSTANCE.from(dto);
        int i = getBaseMapper().updateById(level);
        assert i == 1;
        return dto;
    }

    @Override public Map<String, AlarmLevelDTO> queryLevelMapAll() {
        List<AlarmConfigLevel> alarmConfigLevels = getBaseMapper().selectList(new QueryWrapper<>());
        List<AlarmLevelDTO> levelDTOList = AlarmConfigLevelToDtoConvert.INSTANCE.to(alarmConfigLevels);
        return levelDTOList.stream().filter(Objects::nonNull).filter(p -> StringUtils.isNotBlank(p.getLevelNo()))
                .collect(Collectors.toMap(AlarmLevelDTO::getLevelNo, p -> p, (p1, p2) -> p2));
    }

}
