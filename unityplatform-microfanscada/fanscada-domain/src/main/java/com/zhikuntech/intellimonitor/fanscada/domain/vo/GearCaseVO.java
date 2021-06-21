package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName:
 * @Description:齿轮箱
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GearCaseVO {
    @GoldenId(value = 117)
    @ApiModelProperty("高速轴驱动端温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal highsppedshaftTemperature;

    @GoldenId(value = 108)
    @ApiModelProperty("低速轴驱动端温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal lowspeedshaftTemperature;

    @GoldenId(value = 118)
    @ApiModelProperty("齿轮主轴承温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal gearmainTemperature;

    @GoldenId(value = 109)
    @ApiModelProperty("IMS齿轮轴承温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal imsgearTemperature;

    @GoldenId(value = 110)
    @ApiModelProperty("IMS靠近风轮轴承温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal imswindwheelGearTemperature;

    @GoldenId(value = 111)
    @ApiModelProperty("液压站压力")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal hydraulicstationpressure;

}
