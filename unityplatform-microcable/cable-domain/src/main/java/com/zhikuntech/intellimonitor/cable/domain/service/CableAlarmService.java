package com.zhikuntech.intellimonitor.cable.domain.service;

import com.zhikuntech.intellimonitor.cable.domain.dto.CableTemperatureAlarmDTO;
import com.zhikuntech.intellimonitor.cable.domain.query.AlarmQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CableAlarmService {
    /**
     * 获取当前报警海缆分段的一段时间的温度数据
     * @param  query // id 查1 , date "2021-07-01 15:40:00" 现在就 7月1有数据
     * @return 报警的海缆分段 报警时间前后12小时的温度数据
     */
    List<CableTemperatureAlarmDTO> getAlarmTemperature(AlarmQuery query) throws Exception;
    /**
     * 获取当前报警海缆分段 报警时间 的 整条海缆 的温度数据
     * @param query// id   输入一个点位 判断点位在哪个海缆上ABC date "2021-07-01 15:40:00"
     * @return 一条海缆的报警时间 所有点位的温度数据
     */
    List<CableTemperatureAlarmDTO> getAlarmAllTemperature(AlarmQuery query) throws Exception;
}
