package com.zhikuntech.intellimonitor.alarm.domain.convert;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmRuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.core.commons.convert.BasicObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liukai
 */
@Mapper
public interface AlarmConfigRuleToDtoConvert extends BasicObjectMapper<AlarmConfigRule, AlarmRuleDTO> {

    AlarmConfigRuleToDtoConvert INSTANCE = Mappers.getMapper(AlarmConfigRuleToDtoConvert.class);
}
