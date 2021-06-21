package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName: intelligence-monitor
 * @Description:发电机
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorVO {
    @GoldenId(value = 78)
    @ApiModelProperty("发电机状态")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal generatorstatus;

    @GoldenId(value = 91)
    @ApiModelProperty("V相绕组温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal vphasewindingTemperature;

    @GoldenId(value =89)
    @ApiModelProperty("U相绕组温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal uphasewindingTemperature;

    @GoldenId(value =92 )
    @ApiModelProperty("W相绕组温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal wphasewindingTemperature;


    @GoldenId(value = 93)
    @ApiModelProperty("发电机转速")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ratedGeneratorspeed;

    @GoldenId(value = 97)
    @ApiModelProperty("发电机前轴承温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal forebearingTemperature;

    @GoldenId(value = 98)
    @ApiModelProperty("发电机后轴承温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal backbearingTemperature;

    @GoldenId(value = 99)
    @ApiModelProperty("变压器油温")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal transformer_Oil_Temperature;

    @GoldenId(value = 100)
    @ApiModelProperty("变压器室温")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal transformer_room_temperature;

    @GoldenId(value = 104)
    @ApiModelProperty("变频器冷却剂温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal coolant_Temperature;


}


