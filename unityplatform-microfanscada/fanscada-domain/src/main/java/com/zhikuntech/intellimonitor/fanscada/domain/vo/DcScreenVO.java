package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author: DAI
 * @date: Created in 2021/6/15 18:05
 * @description： 直流屏遥测数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcScreenVO {

    @GoldenId(value = 76)
    @ApiModelProperty("直流屏电池组电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal batteryPackVoltage;

    @GoldenId(value = 77)
    @ApiModelProperty("直流屏电池组温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal batteryPackTemperature;

    @GoldenId(value = 15)
    @ApiModelProperty("直流屏负载电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal loadCurrent;

    @GoldenId(value = 16)
    @ApiModelProperty("直流屏母线负对地电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal negativeVoltage;

    @GoldenId(value = 17)
    @ApiModelProperty("直流屏母线负对地电阻")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal negativeResistance;

    @GoldenId(value = 18)
    @ApiModelProperty("直流屏主机交流输入电压A相")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal voltagePhaseA;

    @GoldenId(value = 19)
    @ApiModelProperty("直流屏主机交流输入电压C相")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal voltagePhaseC;

    @GoldenId(value = 20)
    @ApiModelProperty("直流屏02#UPS/NV输出电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal outputVoltage2;

    @GoldenId(value = 21)
    @ApiModelProperty("直流屏02#UPS/NV输出电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal outputCurrent2;

    @GoldenId(value = 22)
    @ApiModelProperty("直流屏电池组电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal batteryPackCurrent;

    @GoldenId(value = 23)
    @ApiModelProperty("直流屏母线电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal busVoltage;

    @GoldenId(value = 24)
    @ApiModelProperty("直流屏母线正对地电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ratedOutputVoltage;

    @GoldenId(value = 25)
    @ApiModelProperty("直流屏母线正对地电阻")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal positiveVoltage;

    @GoldenId(value = 26)
    @ApiModelProperty("直流屏母线对地交流电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal acVoltage;

    @GoldenId(value = 27)
    @ApiModelProperty("直流屏主机交流输入电压B相")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal voltagePhaseB;

    @GoldenId(value = 28)
    @ApiModelProperty("直流屏01#UPS/NV输出电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal outputVoltage1;

    @GoldenId(value = 29)
    @ApiModelProperty("直流屏01#UPS/NV输出电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal outputCurrent1;
}
