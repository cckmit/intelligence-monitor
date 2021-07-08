package com.zhikuntech.intellimonitor.alarm.domain.convert;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigMonitor;
import com.zhikuntech.intellimonitor.core.commons.convert.BasicObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * AlarmConfigMonitor
 * 转换为
 * AlarmMonitorDTO
 *
 * @author liukai
 */
@Mapper
public interface AlarmConfigMonitorToDtoConvert extends BasicObjectMapper<AlarmConfigMonitor, AlarmMonitorDTO> {

    AlarmConfigMonitorToDtoConvert INSTANCE = Mappers.getMapper(AlarmConfigMonitorToDtoConvert.class);
}
