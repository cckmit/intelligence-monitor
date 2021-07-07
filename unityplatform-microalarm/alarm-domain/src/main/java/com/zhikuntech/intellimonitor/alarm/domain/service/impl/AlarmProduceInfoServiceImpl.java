package com.zhikuntech.intellimonitor.alarm.domain.service.impl;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmStatusGroupByModuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.zhikuntech.intellimonitor.alarm.domain.mapper.AlarmProduceInfoMapper;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 告警信息 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Service
public class AlarmProduceInfoServiceImpl extends ServiceImpl<AlarmProduceInfoMapper, AlarmProduceInfo> implements IAlarmProduceInfoService {


    @Override
    public List<AlarmStatusGroupByModuleDTO> fetchStatusAllGroup() {
        // todo

        return null;
    }
}
