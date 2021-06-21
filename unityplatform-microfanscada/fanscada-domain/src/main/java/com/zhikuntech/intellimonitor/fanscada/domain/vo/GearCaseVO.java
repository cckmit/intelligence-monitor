package com.zhikuntech.intellimonitor.fanscada.domain.vo;

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
    @GoldenId(value = 166)
    @ApiModelProperty("高速轴驱动端温度")
    private BigDecimal highsppedshaftTemperature;

    @GoldenId(value = 167)
    @ApiModelProperty("低速轴驱动端温度")
    private BigDecimal lowspeedshaftTemperature;

    @GoldenId(value = 168)
    @ApiModelProperty("齿轮主轴承温度")
    private BigDecimal gearmainTemperature;

    @GoldenId(value = 169)
    @ApiModelProperty("IMS齿轮轴承温度")
    private BigDecimal imsgearTemperature;

    @GoldenId(value = 170)
    @ApiModelProperty("IMS靠近风轮轴承温度")
    private BigDecimal imswindwheelGearTemperature;

    @GoldenId(value = 171)
    @ApiModelProperty("液压站压力")
    private BigDecimal hydraulicstationpressure;

}
