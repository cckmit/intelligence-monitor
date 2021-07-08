package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigRule;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmConfigRuleMapper;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 规则表 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Service
public class AlarmConfigRuleServiceImpl extends ServiceImpl<AlarmConfigRuleMapper, AlarmConfigRule> implements IAlarmConfigRuleService {



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

}
