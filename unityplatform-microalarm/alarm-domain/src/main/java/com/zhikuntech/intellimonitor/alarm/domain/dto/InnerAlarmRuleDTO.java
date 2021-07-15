package com.zhikuntech.intellimonitor.alarm.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class InnerAlarmRuleDTO {


    @ApiModelProperty("规则编码")
    private String ruleNo;

    /**
     * 告警策略名称
     */
    @ApiModelProperty("告警策略名称")
    private String alarmRuleName;

    /**
     * 告警类型(运行事项/操作记录/系统事件)
     */
    @ApiModelProperty("告警类型(运行事项/操作记录/系统事件)")
    private String alarmType;

    /**
     * 关联测点
     */
    @ApiModelProperty("关联测点")
    private List<AlarmMonitorDTO> withMonitors;

    /**
     * 告警规则值
     */
    @ApiModelProperty("告警规则值")
    private String alarmValue;

    /**
     * 一级预警规则值
     */
    @ApiModelProperty("一级预警规则值")
    private String preAlarmOneValue;

    /**
     * 二级预警规则值
     */
    @ApiModelProperty("二级预警规则值")
    private String preAlarmTwoValue;

    /**
     * 规则类型(0遥信数据/1遥测数据)
     */
    @ApiModelProperty("规则类型(0遥信数据/1遥测数据)")
    private Integer ruleType;

    /**
     * 告警等级编码信息
     */
    @ApiModelProperty("告警等级编码信息")
    private AlarmLevelDTO levelNoAlarmInfo;

    /**
     * 一级预警等级编码信息
     */
    @ApiModelProperty("一级预警等级编码信息")
    private AlarmLevelDTO levelNoOneInfo;

    /**
     * 二级预警等级编码信息
     */
    @ApiModelProperty("二级预警等级编码信息")
    private AlarmLevelDTO levelNoTwoInfo;

    // -----------------------------------数据不展示-----------------------------------

    /**
     * 告警等级编码
     */
    @JsonIgnore
    private String levelNoAlarm;

    /**
     * 一级预警等级编码
     */
    @JsonIgnore
    private String levelNoOne;

    /**
     * 二级预警等级编码
     */
    @JsonIgnore
    private String levelNoTwo;

}
