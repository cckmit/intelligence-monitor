package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author 代志豪
 * 2021/6/7 16:24
 */
@Data
@Accessors(chain = true)
public class FanStatisticsDTO {

    /**
     * 风机台数
     */
    private Integer num;

    /**
     * 装机容量
     */
    private BigDecimal capacity;

    /**
     * 当前有功功率
     */
    @GoldenId(value = 2)
    private BigDecimal activePower;

    /**
     * 平均风速
     */
    @GoldenId(value = 1)
    private BigDecimal averageWindVelocity;

    /**
     * 日发电量
     */
    private BigDecimal dailyPowerGeneration;

    /**
     * 月发电量
     */
    private BigDecimal monthlyPowerGeneration;

    /**
     * 年发电量
     */
    private BigDecimal annualPowerGeneration;

    /**
     * 日上网电量
     */
    private BigDecimal dailyOnlinePower;

    /**
     * 月上网电量
     */
    private BigDecimal monthlyOnlinePower;

    /**
     * 年上网电量
     */
    private BigDecimal annualOnlinePower;

    /**
     * 能量输出
     */
    @JsonIgnore
    @GoldenId(value = 13)
    private BigDecimal energyOutput;

    /**
     * 反向有功
     */
    @JsonIgnore
    @GoldenId(value = 14)
    private BigDecimal reverseActivePower;
}
