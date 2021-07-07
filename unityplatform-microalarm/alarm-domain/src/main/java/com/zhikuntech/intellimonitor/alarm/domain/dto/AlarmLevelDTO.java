package com.zhikuntech.intellimonitor.alarm.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 告警等级信息
 *
 * @author liukai
 */
@Data
@ApiModel("告警等级信息")
public class AlarmLevelDTO {

    @ApiModelProperty("id标识")
    private Integer id;

    /**
     * 等级编码
     */
    @ApiModelProperty("等级编码")
    private String levelNo;

    /**
     * 告警等级(1/2/3)
     */
    @ApiModelProperty("告警等级(1/2/3)")
    private Integer alarmLevel;

    /**
     * 告警说明(重要/一般/告知)
     */
    @ApiModelProperty("告警说明(重要/一般/告知)")
    private String levelIllustrate;

    /**
     * 告警方式(闪烁/弹窗)
     */
    @ApiModelProperty("告警方式")
    private String alarmWay;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String mark;


}
