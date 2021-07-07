package com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 新增告警等级
 *
 * @author liukai
 */
@Data
@ApiModel("新增告警等级")
public class AddNewAlarmLevelQuery {

    @ApiModelProperty(value = "告警等级名称", required = true)
    @NotBlank
    private String alarmLevelName;

    @ApiModelProperty(value = "告警方式, 多个使用[,]分割", required = true)
    @NotBlank
    private String alarmWay;

    @ApiModelProperty(value = "备注", required = false)
    private String mark;

}
