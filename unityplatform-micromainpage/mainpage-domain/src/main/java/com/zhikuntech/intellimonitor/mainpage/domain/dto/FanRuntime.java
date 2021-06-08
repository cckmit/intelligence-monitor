package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 代志豪
 * @date 2021-06-07
 */

@Data
@Accessors(chain = true)
public class FanRuntime  {

    private Integer id;

    /**
     * 风机编号
     */
    private Integer number;

    /**
     * 风速
     */
    private Double windVelocity;

    /**
     * 风向
     */
    private Double windDirection;

    /**
     * 有功功率
     */
    private Double activePower;

    /**
     * 无功功率
     */
    private Double reactivePower;

    /**
     * 功率因数
     */
    private Double powerFactor;

    /**
     * 频率
     */
    private Double frequency;

    /**
     * U相电流
     */
    private Double iu;

    /**
     * V相电流
     */
    private Double iv;

    /**
     * W相电流
     */
    private Double iw;

    /**
     * U相电压
     */
    private Double uu;

    /**
     * V相电压
     */
    private Double uv;

    /**
     * W相电压
     */
    private Double uw;

    /**
     * 运行状态
     */
    private String runningStatus;

    /**
     * 浆距角A
     */
    private Double angelA;

    /**
     * 浆距角B
     */
    private Double angelB;

    /**
     * 浆距角C
     */
    private Double angelC;

    /**
     * 故障代码
     */
    private Integer errorCode;

    /**
     * 前轴承温度
     */
    private Double frontBearingTemp;

    /**
     * 后轴承温度
     */
    private Double rearBearingTemp;

    /**
     * 发电机转速
     */
    private Double generatorRate;

    /**
     * 变压器油温
     */
    private Double transformerOilTemp;

    /**
     * 变压器室温
     */
    private Double transformerTemp;

    /**
     * 变频器冷却水温度
     */
    private Double inverterWaterTemp;

    /**
     * 轮毂温度
     */
    private Double hubTemp;

    /**
     * 轮毂主轴承温度
     */
    private Double hubBearingTemp;

    /**
     * 液压油温
     */
    private Double hydraulicOilTemp;

    /**
     * 高速轴驱动温度
     */
    private Double highShaftDriveTemp;

    /**
     * 低速轴驱动温度
     */
    private Double lowShaftDriveTemp;

    /**
     * 齿轮主轴承温度
     */
    private Double gearBearingTemp;

    /**
     * IMS靠近风轮轴承温度
     */
    private Double imsBearingTemp;

    /**
     * 液压站压力
     */
    private Double hydraulicStationPressure;



}
