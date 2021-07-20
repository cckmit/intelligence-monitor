package com.zhikuntech.intellimonitor.cable.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("海缆运行实时温度数据")
public class CableRunTimeTemperatureDTO {
    /**
     * 海缆温度分段编号
     */
    @ApiModelProperty("海缆温度分段编号")
    private Integer number;

    /**
     * 温度
     */
    @GoldenId(value = 1)
    @ApiModelProperty("海缆分段温度")
    private BigDecimal temperature;
}
