package com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("告警信息批量查询")
public class AlarmInfoLimitQuery implements Serializable {

    private static final long serialVersionUID = 8948437571026272809L;

    @ApiModelProperty(value = "数据条数(最大30条), 不能为0", required = true)
    private Integer dataNum;

    @ApiModelProperty(value = "行号", required = true)
    private Long rowNum;

    @ApiModelProperty(value = "查询类型(0全部告警,1待确认重要告警)", required = true)
    private Integer queryType;

}
