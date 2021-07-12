package com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 新增告警规则
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("新增告警规则")
public class AddNewAlarmRuleQuery {

    /**
     * 告警策略名称
     */
    @ApiModelProperty(value = "告警策略名称", required = true)
    private String alarmRuleName;

    /**
     * 关联测点
     */
    @ApiModelProperty(value = "关联测点", required = true)
    private List<String> monitors;

    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型", required = true)
    private String alarmType;

    /**
     * 告警区间
     */
    @ApiModelProperty(value = "告警区间", required = true)
    private String alarmRegion;

    /**
     * 告警区间-关联告警等级
     */
    @ApiModelProperty(value = "告警区间-关联告警等级", required = true)
    private String alarmRegionConnLevel;

    /**
     * 规则类型(0遥信数据/1遥测数据)
     */
    @ApiModelProperty(value = "规则类型(0遥信数据/1遥测数据)", required = true)
    private Integer ruleType;

    // ------------------------------------------------ 以上必传

    /**
     * 一次预警值
     */
    @ApiModelProperty(value = "一次预警值")
    private String preAlarmOne;

    /**
     * 一次预警值-关联告警等级
     */
    @ApiModelProperty(value = "一次预警值-关联告警等级")
    private String preAlarmOneConnLevel;

    /**
     * 二次预警
     */
    @ApiModelProperty(value = "二次预警")
    private String preAlarmTwo;

    /**
     * 二次预警-关联告警等级
     */
    @ApiModelProperty(value = "二次预警-关联告警等级")
    private String preAlarmTwoConnLevel;

}
