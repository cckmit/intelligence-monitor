package com.zhikuntech.intellimonitor.alarm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("告警信息")
public class AlarmInfoBatchDTO {

    /*
        告警信息编码/告警时间/告警描述/告警等级/告警类型/是否已确认/确认人/确认时间/恢复时间
     */

    @ApiModelProperty("告警信息编码")
    private String alarmInfoNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("告警时间,格式[yyyy-MM-dd HH:mm:ss]")
    private LocalDateTime alarmTime;

    /**
     * 告警描述规则如下：
     *
     *    “【场站名】+【测点名】+描述”，比如“普陀风场陆上计量站#1 主变油温越限，当前值80℃”
     *
     *    告警描述
     */
    @ApiModelProperty("告警描述")
    private String alarmDescribe;

    @ApiModelProperty("告警等级")
    private String alarmLevel;

    @ApiModelProperty("告警类型")
    private String alarmType;

    @ApiModelProperty("是否已确认")
    private Integer confirm;

    @ApiModelProperty("确认人")
    private String confirmPerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("确认时间")
    private LocalDateTime confirmTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("恢复时间")
    private LocalDateTime restoreTime;

    @ApiModelProperty("行号")
    private Long rowNum;

}
