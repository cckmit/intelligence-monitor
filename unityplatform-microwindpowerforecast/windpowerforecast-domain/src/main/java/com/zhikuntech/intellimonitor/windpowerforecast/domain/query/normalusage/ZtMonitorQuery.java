package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage;

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
@ApiModel("曲线展示-状态展示")
public class ZtMonitorQuery {
    @ApiModelProperty(value = "时间字符串:[yyyy-MM-dd zz:xx:cc]", required = true)
    private String dateStr;
}
