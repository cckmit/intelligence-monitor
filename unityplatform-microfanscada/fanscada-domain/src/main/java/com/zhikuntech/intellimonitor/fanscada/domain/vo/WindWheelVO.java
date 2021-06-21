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
 * @Description:
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindWheelVO {

    @GoldenId(value = 80)
    @ApiModelProperty("桨距角A")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal kexAngle_A;

    @GoldenId(value = 81)
    @ApiModelProperty("桨距角B")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal kexAngle_B;

    @GoldenId(value = 82)
    @ApiModelProperty("桨距角C")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal kexAngle_C;


    @GoldenId(value = 83)
    @ApiModelProperty("风轮转速")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal ratedRotSpeed;

    @GoldenId(value = 84)
    @ApiModelProperty("有功功率")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal activepower;

    @GoldenId(value = 85)
    @ApiModelProperty("无功功率")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal reactivepower;

    @GoldenId(value = 86)
    @ApiModelProperty("风机频率")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal fqcy;

    @GoldenId(value = 87)
    @ApiModelProperty("风机风速")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal windspeed;

    @GoldenId(value = 88)
    @ApiModelProperty("风向")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal winddirection;

    @GoldenId(value = 90)
    @ApiModelProperty("故障代码")
    private Integer errorCode;

    @GoldenId(value = 79)
    @ApiModelProperty("功率因数")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal powernum;

    @GoldenId(value = 101)
    @ApiModelProperty("U相电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal uphaseV;

    @GoldenId(value = 102)
    @ApiModelProperty("V相电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal vphaseV;

    @GoldenId(value = 103)
    @ApiModelProperty("W相电压")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal wphaseV;

    @GoldenId(value = 94)
    @ApiModelProperty("U相电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal uphaseA;

    @GoldenId(value = 95)
    @ApiModelProperty("V相电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal vphaseA;

    @GoldenId(value = 96)
    @ApiModelProperty("W相电流")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal wphaseA;

    @GoldenId(value = 112)
    @ApiModelProperty("叶片油压A")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal bladeperssureA;

    @GoldenId(value = 113)
    @ApiModelProperty("叶片油压B")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal bladeperssureB;

    @GoldenId(value = 114)
    @ApiModelProperty("叶片油压C")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal bladeperssureC;

}
