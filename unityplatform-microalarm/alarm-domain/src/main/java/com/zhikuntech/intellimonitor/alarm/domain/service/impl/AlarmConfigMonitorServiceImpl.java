package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    @Override
    public Pager<AlarmMonitorDTO> queryByPage(AlarmMonitorSimpleQuery query) {
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("查询参数不能为空.");
        }
        Page<AlarmConfigMonitor> page = new Page<>(query.getPageNumber(), query.getPageSize());
        QueryWrapper<AlarmConfigMonitor> criteria = new QueryWrapper<>();
        if (StringUtils.isNotBlank(query.getMonitorDescribe())) {
            criteria.like("monitor_describe", StringUtils.trim(query.getMonitorDescribe()));
        }
        Page<AlarmConfigMonitor> pageResults = getBaseMapper().selectPage(page, criteria);
        List<AlarmConfigMonitor> records = pageResults.getRecords();
        // 转换结果
        List<AlarmMonitorDTO> dtoList = AlarmConfigMonitorToDtoConvert.INSTANCE.to(records);
        return new Pager<>(dtoList);
    }
}
