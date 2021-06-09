package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.annotation.GoldenId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 代志豪
 * @date 2021-06-07
 */

@Data
@Accessors(chain = true)
public class FanRuntimeDto {

    @JsonIgnore
    private Integer id;

    /**
     * 风机编号
     */
    private Integer number;

    /**
     * 风速
     */
    @GoldenId(value = 30)
    private Double windVelocity;

    /**
     * 风向
     */
    @JsonIgnore
    private Double windDirection;

    /**
     * 功率因数
     */
    @JsonIgnore
    private Double powerFactor;

    /**
     * 有功功率
     */
    @GoldenId(value = 31)
    private Double activePower;

    /**
     * 无功功率
     */
    @JsonIgnore
    private Double reactivePower;

    /**
     * 发电机转速
     */
    @GoldenId(value = 32)
    private Double generatorRate;

    /**
     * 频率
     */
    @JsonIgnore
    private Double frequency;

    /**
     * 月发电量
     */
    @GoldenId(value = 42)
    private Double monthlyPowerGeneration;

    /**
     * 运行状态
     */
    @GoldenId(value = 43)
    private Long runningStatus;

    /**
     * 故障代码
     */
    @GoldenId(value = 44)
    private Long errorCode;

    /**
     * 前轴承温度
     */
    @GoldenId(value = 35)
    private Double frontBearingTemp;

    /**
     * 后轴承温度
     */
    @GoldenId(value = 36)
    private Double rearBearingTemp;

    /**
     * 变压器油温
     */
    @GoldenId(value = 39)
    private Double transformerOilTemp;

    /**
     * 变压器室温
     */
    @GoldenId(value = 38)
    private Double transformerTemp;

    /**
     * 浆距角A
     */
    @JsonIgnore
    private Double angelA;

    /**
     * 浆距角B
     */
    @JsonIgnore
    private Double angelB;

    /**
     * 浆距角C
     */
    @JsonIgnore
    private Double angelC;

    /**
     * U相电流
     */
    @JsonIgnore
    private Double iu;

    /**
     * V相电流
     */
    @JsonIgnore
    private Double iv;

    /**
     * W相电流
     */
    @JsonIgnore
    private Double iw;

    /**
     * U相电压
     */
    @JsonIgnore
    private Double uu;

    /**
     * V相电压
     */
    @JsonIgnore
    private Double uv;

    /**
     * W相电压
     */
    @JsonIgnore
    private Double uw;
    /**
     * 变频器冷却水温度
     */
    @JsonIgnore
    private Double inverterWaterTemp;

    /**
     * 轮毂温度
     */
    @JsonIgnore
    private Double hubTemp;

    /**
     * 轮毂主轴承温度
     */
    @JsonIgnore
    private Double hubBearingTemp;

    /**
     * 液压油温
     */
    @JsonIgnore
    private Double hydraulicOilTemp;

    /**
     * 高速轴驱动温度
     */
    @JsonIgnore
    private Double highShaftDriveTemp;

    /**
     * 低速轴驱动温度
     */
    @JsonIgnore
    private Double lowShaftDriveTemp;

    /**
     * 齿轮主轴承温度
     */
    @GoldenId(value = 37)
    private Double gearBearingTemp;

    /**
     * IMS靠近风轮轴承温度
     */
    @JsonIgnore
    private Double imsBearingTemp;

    /**
     * 液压站压力
     */
    @JsonIgnore
    private Double hydraulicStationPressure;

}
