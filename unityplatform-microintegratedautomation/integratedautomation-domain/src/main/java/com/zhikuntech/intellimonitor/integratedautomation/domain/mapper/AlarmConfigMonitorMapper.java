package com.zhikuntech.intellimonitor.integratedautomation.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikuntech.intellimonitor.integratedautomation.domain.po.AlarmConfigMonitorPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 杨锦程
 * @Date 2021/7/22 11:21
 * @Description 告警配置
 * @Version 1.0
 */
@Mapper
public interface AlarmConfigMonitorMapper extends BaseMapper<AlarmConfigMonitorPO> {
}
