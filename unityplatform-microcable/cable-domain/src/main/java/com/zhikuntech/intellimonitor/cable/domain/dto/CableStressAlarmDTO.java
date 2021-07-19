package com.zhikuntech.intellimonitor.cable.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel("海缆应力告警历史数据")
public class CableStressAlarmDTO {
    /**
     * 海缆分段编号
     */
    @ApiModelProperty("海缆编号")
    private Integer number;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    private Date date;

    /**
     * 温度
     */
    @ApiModelProperty("海缆分段温度")
    private BigDecimal stress;
}
