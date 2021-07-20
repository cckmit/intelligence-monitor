package com.zhikuntech.intellimonitor.alarm.domain.dto.external;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测点初始状态
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("测点初始状态")
public class CurrentStatusByMonitorDTO {

    @ApiModelProperty("测点id")
    private String monitorId;

    @ApiModelProperty("测点名称")
    private String monitorName;

    @ApiModelProperty("是否闪烁(只要状态发生变化就会闪烁)true/false")
    private Boolean isFlash;

    @ApiModelProperty("颜色变化")
    private String textColor;

    @ApiModelProperty("是否是报警状态true/false")
    private Boolean isAlarmStatus;

    @ApiModelProperty("报警时间 返回格式：时间戳  毫秒级")
    private Long alarmTime;

}
