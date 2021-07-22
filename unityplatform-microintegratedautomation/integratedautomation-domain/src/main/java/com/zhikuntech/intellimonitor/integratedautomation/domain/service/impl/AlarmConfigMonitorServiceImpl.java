package com.zhikuntech.intellimonitor.integratedautomation.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.integratedautomation.domain.mapper.AlarmConfigMonitorMapper;
import com.zhikuntech.intellimonitor.integratedautomation.domain.po.AlarmConfigMonitorPO;
import com.zhikuntech.intellimonitor.integratedautomation.domain.service.AlarmConfigMonitorService;
import org.springframework.stereotype.Service;

/**
 * @Author 杨锦程
 * @Date 2021/7/22 11:28
 * @Description 测点Service
 * @Version 1.0
 */
@Service
public class AlarmConfigMonitorServiceImpl extends ServiceImpl<AlarmConfigMonitorMapper, AlarmConfigMonitorPO>
        implements AlarmConfigMonitorService {
}
