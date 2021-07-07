package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.alarm.domain.convert.AlarmConfigLevelToDtoConvert;
import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigLevel;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigLevelMapper;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AddNewAlarmLevelQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AlarmLevelSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import org.springframework.stereotype.Service;

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
@Service
public class AlarmConfigLevelServiceImpl extends ServiceImpl<AlarmConfigLevelMapper, AlarmConfigLevel> implements IAlarmConfigLevelService {

    @Override
    public boolean addNewAlarmLevel(AddNewAlarmLevelQuery query) {
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

    @Override
    public Pager<AlarmLevelDTO> queryAlarmLevel(AlarmLevelSimpleQuery query) {
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


}
