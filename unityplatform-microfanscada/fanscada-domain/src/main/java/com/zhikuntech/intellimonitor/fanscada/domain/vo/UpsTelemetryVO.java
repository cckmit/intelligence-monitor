package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
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

    @GoldenId(value = 97)
    @ApiModelProperty("UPS输入电压")
    private BigDecimal inputVoltage;

    @GoldenId(value = 68)
    @ApiModelProperty("UPS输出电压")
    private BigDecimal outputVoltage;

    @GoldenId(value = 69)
    @ApiModelProperty("UPS旁路功率")
    private BigDecimal bypassPower;

    @GoldenId(value = 70)
    @ApiModelProperty("UPS输出电流")
    private BigDecimal outputCurrent;

    @GoldenId(value = 71)
    @ApiModelProperty("UPS环境温度")
    private BigDecimal ambientTemperature;

    @GoldenId(value = 72)
    @ApiModelProperty("UPS额定输出电流")
    private BigDecimal ratedOutputCurrent;

    @GoldenId(value = 73)
    @ApiModelProperty("UPS额定频率")
    private BigDecimal ratedFrequency;

    @GoldenId(value = 74)
    @ApiModelProperty("UPS旁路电压")
    private BigDecimal bypassVoltage;

    @GoldenId(value = 75)
    @ApiModelProperty("UPS输入功率")
    private BigDecimal inputPower;

    @GoldenId(value = 76)
    @ApiModelProperty("UPS输出功率")
    private BigDecimal outputPower;

    @GoldenId(value = 77)
    @ApiModelProperty("UPS电池电压")
    private BigDecimal batteryVoltage;

    @GoldenId(value = 78)
    @ApiModelProperty("UPS额定输出电压")
    private BigDecimal ratedOutputVoltage;

    @GoldenId(value = 79)
    @ApiModelProperty("UPS额定电池电压")
    private BigDecimal ratedBatteryVoltage;
}
