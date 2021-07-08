package com.zhikuntech.intellimonitor.alarm.domain.query.alarmmonitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 测点信息查询
 *
 * @author liukai
 */
@Data
@ApiModel("测点信息")
public class AlarmMonitorSimpleQuery {

    @ApiModelProperty(value = "测点描述")
    private String monitorDescribe;

    @ApiModelProperty(value = "页码", required = true)
    @NotNull
    private Integer pageNumber;

    @ApiModelProperty(value = "每页数量", required = true)
    @NotNull
    private Integer pageSize;

}
