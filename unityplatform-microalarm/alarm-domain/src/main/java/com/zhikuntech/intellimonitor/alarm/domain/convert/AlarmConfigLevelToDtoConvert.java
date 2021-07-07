package com.zhikuntech.intellimonitor.alarm.domain.convert;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigLevel;
import com.zhikuntech.intellimonitor.core.commons.convert.BasicObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * AlarmConfigLevel
 * 转换为
 * AlarmLevelDTO
 *
 * @author liukai
 */
@Mapper
public interface AlarmConfigLevelToDtoConvert extends BasicObjectMapper<AlarmConfigLevel, AlarmLevelDTO> {

    AlarmConfigLevelToDtoConvert INSTANCE = Mappers.getMapper(AlarmConfigLevelToDtoConvert.class);
}
