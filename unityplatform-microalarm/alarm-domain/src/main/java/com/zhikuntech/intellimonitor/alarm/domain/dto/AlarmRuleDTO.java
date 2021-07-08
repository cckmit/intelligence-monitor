package com.zhikuntech.intellimonitor.alarm.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 告警规则
 *
 * @author liukai
 */
@Data
@ApiModel("告警规则")
public class AlarmRuleDTO {


    @ApiModelProperty("规则编码")
    private String ruleNo;

    /**
     * 告警策略名称
     */
    private String alarmRuleName;

    /**
     * 告警类型(运行事项/操作记录/系统事件)
     */
    private String alarmType;

    /**
     * 关联测点
     */
    private List<AlarmMonitorDTO> withMonitors;

    // todo

}
