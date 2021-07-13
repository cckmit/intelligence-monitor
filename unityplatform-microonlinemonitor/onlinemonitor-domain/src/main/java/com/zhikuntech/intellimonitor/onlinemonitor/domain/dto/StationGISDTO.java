package com.zhikuntech.intellimonitor.onlinemonitor.domain.dto;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author 杨锦程
 * @Date 2021/7/9 14:30
 * @Description 陆上计量站(海上升压站)GIS
 * 遥信 bool
 * @Version 1.0
 */
@Data
@ApiModel("陆上计量站(海上升压站)GIS")
public class StationGISDTO {
    /**
     * SF6浓度值(ppm)
     */
    @GoldenId(1)
    @ApiModelProperty("SF6浓度值(ppm)")
    private BigDecimal sf6Density;

    /**
     * O2浓度值(ppm)
     */
    @GoldenId(2)
    @ApiModelProperty("O2浓度值(ppm)")
    private BigDecimal o2Density;

    /**
     * 采集点
     */
    @ApiModelProperty("采集点")
    private Integer patchPoint;
}
