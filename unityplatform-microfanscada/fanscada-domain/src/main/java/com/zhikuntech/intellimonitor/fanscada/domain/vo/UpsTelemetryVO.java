package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author: DAI
 * @date: Created in 2021/6/15 17:39
 * @description： UPS遥测数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsTelemetryVO {

    @GoldenId(value = 30)
    @ApiModelProperty("UPS输入电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal inputVoltage;

    @GoldenId(value = 64)
    @ApiModelProperty("UPS输出电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal outputVoltage;

    @GoldenId(value = 65)
    @ApiModelProperty("UPS旁路功率")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal bypassPower;

    @GoldenId(value = 66)
    @ApiModelProperty("UPS输出电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal outputCurrent;

    @GoldenId(value = 67)
    @ApiModelProperty("UPS环境温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ambientTemperature;

    @GoldenId(value = 68)
    @ApiModelProperty("UPS额定输出电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ratedOutputCurrent;

    @GoldenId(value = 69)
    @ApiModelProperty("UPS额定频率")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ratedFrequency;

    @GoldenId(value = 70)
    @ApiModelProperty("UPS旁路电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal bypassVoltage;

    @GoldenId(value = 71)
    @ApiModelProperty("UPS输入功率")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal inputPower;

    @GoldenId(value = 72)
    @ApiModelProperty("UPS输出功率")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal outputPower;

    @GoldenId(value = 73)
    @ApiModelProperty("UPS电池电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal batteryVoltage;

    @GoldenId(value = 74)
    @ApiModelProperty("UPS额定输出电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ratedOutputVoltage;

    @GoldenId(value = 75)
    @ApiModelProperty("UPS额定电池电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ratedBatteryVoltage;
}
