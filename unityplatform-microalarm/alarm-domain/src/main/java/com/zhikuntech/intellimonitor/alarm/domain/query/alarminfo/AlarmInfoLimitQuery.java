package com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liukai
 */
@Data
@ApiModel("告警信息批量查询")
public class AlarmInfoLimitQuery implements Serializable {

    private static final long serialVersionUID = 8948437571026272809L;

    @ApiModelProperty(value = "数据量(最大30条), 不能为0", required = true)
    private Integer dataNum;

    @ApiModelProperty(value = "行号", required = true)
    private Long rowNum;



}