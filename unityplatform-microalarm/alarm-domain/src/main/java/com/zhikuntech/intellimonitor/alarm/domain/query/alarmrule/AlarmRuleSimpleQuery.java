package com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 告警规则查询
 *
 * @author liukai
 */
@Data
@ApiModel("告警规则查询")
public class AlarmRuleSimpleQuery {

    @ApiModelProperty(value = "规则类型(0遥信数据/1遥测数据)", required = true)
    @NotNull
    private Integer ruleType;

    @ApiModelProperty(value = "页码", required = true)
    @NotNull
    private Integer pageNumber;

    @ApiModelProperty(value = "每页数量", required = true)
    @NotNull
    private Integer pageSize;
}
