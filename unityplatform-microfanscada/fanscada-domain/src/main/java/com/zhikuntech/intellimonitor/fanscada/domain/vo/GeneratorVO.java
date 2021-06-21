package com.zhikuntech.intellimonitor.fanscada.domain.vo;

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
    @GoldenId(value = 134)
    @ApiModelProperty("发电机状态")
    private BigDecimal generatorstatus;

    @GoldenId(value = 147)
    @ApiModelProperty("V相绕组温度")
    private BigDecimal vphasewindingTemperature;

    @GoldenId(value = 145)
    @ApiModelProperty("U相绕组温度")
    private BigDecimal uphasewindingTemperature;

    @GoldenId(value =148 )
    @ApiModelProperty("W相绕组温度")
    private BigDecimal wphasewindingTemperature;


    @GoldenId(value = 149)
    @ApiModelProperty("发电机转速")
    private BigDecimal ratedGeneratorspeed;

    @GoldenId(value = 153)
    @ApiModelProperty("发电机前轴承温度")
    private BigDecimal forebearingTemperature;

    @GoldenId(value = 154)
    @ApiModelProperty("发电机后轴承温度")
    private BigDecimal backbearingTemperature;

    @GoldenId(value = 155)
    @ApiModelProperty("变压器油温")
    private BigDecimal transformer_Oil_Temperature;

    @GoldenId(value = 156)
    @ApiModelProperty("变压器室温")
    private BigDecimal transformer_room_temperature;

    @GoldenId(value = 160)
    @ApiModelProperty("变频器冷却剂温度")
    private BigDecimal coolant_Temperature;


}


