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

    @ApiModelProperty("是否闪烁(标识有报警信息)")
    private Boolean isFlash;

    @ApiModelProperty("颜色变化")
    private String textColor;

}
