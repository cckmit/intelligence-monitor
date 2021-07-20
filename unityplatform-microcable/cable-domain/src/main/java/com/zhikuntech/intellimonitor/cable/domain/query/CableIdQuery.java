package com.zhikuntech.intellimonitor.cable.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("海缆id")
public class CableIdQuery {
    /**
     * 告警类型温度/应力
     */
    @ApiModelProperty(value = "告警类型温度1/应力2", required = true)
    private Integer type;

    /**
     * 告警海缆编号1/2/3
     */
    @ApiModelProperty(value = "告警海缆编号1/2/3", required = true)
    private Integer id;

    /**
     * 告警海缆的第几个点
     */
    @ApiModelProperty(value = "告警点位", required = true)
    private Integer num;

    /**
     * 告警时间
     */
    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd HH:mm:ss]", required = true)
    private String date;
}
