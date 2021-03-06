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
@ApiModel("根据告警信息查询")
public class AlarmQuery {
    /**
     * 告警海缆id
     */
    @ApiModelProperty(value = "告警海缆编号", required = true)
    private Integer id;

    /**
     * 告警时间
     */
    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd HH:mm:ss]", required = true)
    private String date;
}
