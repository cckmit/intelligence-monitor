package com.zhikuntech.intellimonitor.alarm.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 告警数量状态
 *
 * @author liukai
 */
@Data
@ApiModel("告警数量状态")
public class AlarmStatusGroupByModuleDTO {

    /**
     * 组id(模块id)
     */
    @ApiModelProperty("组id(模块id)")
    private Integer groupType;

    /**
     * 组名称(模块名称)
     */
    @ApiModelProperty("组名称(模块名称)")
    private String groupName;

    /**
     * 告警数量
     */
    @ApiModelProperty("告警数量")
    private Integer alarmNum;

}
