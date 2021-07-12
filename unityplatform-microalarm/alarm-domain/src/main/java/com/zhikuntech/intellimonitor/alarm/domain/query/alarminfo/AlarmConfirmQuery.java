package com.zhikuntech.intellimonitor.alarm.domain.query.alarminfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 告警确认
 *
 * @author liukai
 */
@Data
@ApiModel("告警确认")
public class AlarmConfirmQuery {


    @ApiModelProperty("确认人")
    private String confirmPerson;

    @ApiModelProperty("确认类型(0非页码确认1页面确认)")
    private Integer confirmType;

    @ApiModelProperty("页面类型")
    private Integer pageType;

    @ApiModelProperty("告警信息编码")
    private List<String> infoNos;


}
