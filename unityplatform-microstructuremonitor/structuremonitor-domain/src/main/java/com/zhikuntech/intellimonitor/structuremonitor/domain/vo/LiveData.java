package com.zhikuntech.intellimonitor.structuremonitor.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 滕楠
 * @className LiveData
 * @create 2021/7/16 17:20
 **/
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveData {

    @ApiModelProperty("测点1x加速度")
    private BigDecimal a1xAcceleration;

    @ApiModelProperty("测点1y加速度")
    private BigDecimal a1yAcceleration;

    @ApiModelProperty("测点2x加速度")
    private BigDecimal a2xAcceleration;

    @ApiModelProperty("测点2y加速度")
    private BigDecimal a2yAcceleration;

    @ApiModelProperty("测点3x加速度")
    private BigDecimal a3xAcceleration;

    @ApiModelProperty("测点3y加速度")
    private BigDecimal a3yAcceleration;

    @ApiModelProperty("测点4x加速度")
    private BigDecimal a4xAcceleration;

    @ApiModelProperty("测点4y加速度")
    private BigDecimal a4yAcceleration;

    @ApiModelProperty("测点5x加速度")
    private BigDecimal a5xAcceleration;

    @ApiModelProperty("测点5y加速度")
    private BigDecimal a5yAcceleration;

    @ApiModelProperty("测点6x加速度")
    private BigDecimal a6xAcceleration;

    @ApiModelProperty("测点6y加速度")
    private BigDecimal a6yAcceleration;

    @ApiModelProperty("1沉降")
    private BigDecimal a1Subside;

    @ApiModelProperty("2沉降")
    private BigDecimal a2Subside;

    @ApiModelProperty("3沉降")
    private BigDecimal a3Subside;

    @ApiModelProperty("4沉降")
    private BigDecimal a4Subside;
}