package com.zhikuntech.intellimonitor.alarm.domain.dto.external;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取报警组对应的报警数量
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("获取报警组对应的报警数量")
public class FetchAlarmNumWithGroupDTO {

    @ApiModelProperty("组名称")
    private String groupName;

    @ApiModelProperty("告警数量")
    private Integer count;
}
