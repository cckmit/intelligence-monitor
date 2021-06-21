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
 * @Description:风轮机
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WheelSpiderVO {

    @GoldenId(value = 105)
    @ApiModelProperty("偏航状态")
    private Boolean yawingStatus;

    @GoldenId(value = 106)
    @ApiModelProperty("偏航角度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal yawingAngle;

    @GoldenId(value = 115)
    @ApiModelProperty("轮毂温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal wheelSpiderTemperature;

    @GoldenId(value = 116)
    @ApiModelProperty("轮毂主轴承温度")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal wheelmainTemperature;

    @GoldenId(value = 107)
    @ApiModelProperty("液压油温")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private BigDecimal hydraulicTemperature;



}
