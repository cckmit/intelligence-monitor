package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.annotation.GoldenId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 代志豪
 * 2021/6/7 16:24
 */
@Data
@Accessors(chain = true)
public class FanStatisticsDto {

    /**
     * 风机台数
     */
    private Integer num;

    /**
     * 装机容量
     */
    private Double capacity;

    /**
     * 当前有功功率
     */
    private Double activePower;

    /**
     * 平均风速
     */
    private Double averageWindVelocity;

    /**
     * 日发电量
     */
    private Double dailyPowerGeneration;

    /**
     * 月发电量
     */
    private Double monthlyPowerGeneration;

    /**
     * 年发电量
     */
    private Double annualPowerGeneration;

    /**
     * 日上网电量
     */
    private Double dailyOnlinePower;

    /**
     * 月上网电量
     */
    private Double monthlyOnlinePower;

    /**
     * 年上网电量
     */
    private Double annualOnlinePower;

    /**
     * 能量输出
     */
    @JsonIgnore
    @GoldenId(value = 45)
    private Double energyOutput;

    /**
     * 反向有功
     */
    @JsonIgnore
    @GoldenId(value = 46)
    private Double reverseActivePower;
}
