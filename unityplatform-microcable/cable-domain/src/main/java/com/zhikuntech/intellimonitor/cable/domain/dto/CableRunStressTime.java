package com.zhikuntech.intellimonitor.cable.domain.dto;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("海缆运行实时应力数据")
public class CableRunStressTime {
    /**
     * 海缆应力分段编号
     */
    @ApiModelProperty("海缆编号")
    private Integer number;

    /**
     * 应力
     */
    @GoldenId(value = 2)
    @ApiModelProperty("海缆分段应力")
    private BigDecimal stress;
}
