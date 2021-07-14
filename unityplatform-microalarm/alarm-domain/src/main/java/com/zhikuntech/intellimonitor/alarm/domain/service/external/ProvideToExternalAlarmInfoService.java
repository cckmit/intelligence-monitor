package com.zhikuntech.intellimonitor.alarm.domain.service.external;

import com.zhikuntech.intellimonitor.alarm.domain.dto.external.CurrentStatusByMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.external.FetchAlarmNumWithGroupDTO;

import java.util.List;

/**
 * @author liukai
 */
public interface ProvideToExternalAlarmInfoService {

    /**
     * 获取报警组对应的报警数量
     * @param groupNos  对应的组信息
     * @return 组对应的告警数量
     */
    List<FetchAlarmNumWithGroupDTO> fetchWithGroup(List<String> groupNos);

    /**
     * 获取页面上所有测点的状态
     * @param monitorIds    监测点组
     * @return 测点的当前状态
     */
    List<CurrentStatusByMonitorDTO> fetchMonitorCurrentStatus(List<String> monitorIds);

}
