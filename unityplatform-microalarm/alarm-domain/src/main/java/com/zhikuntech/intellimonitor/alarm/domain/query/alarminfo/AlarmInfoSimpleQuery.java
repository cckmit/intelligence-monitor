package com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 告警信息查询
 *
 * @author liukai
 */
@Data
@ApiModel("告警信息查询")
public class AlarmInfoSimpleQuery {

    //# 分页查询条件

    @ApiModelProperty(value = "页码", required = true)
    @NotNull
    private Integer pageNumber;

    @ApiModelProperty(value = "每页数量", required = true)
    @NotNull
    private Integer pageSize;

    //#



}
