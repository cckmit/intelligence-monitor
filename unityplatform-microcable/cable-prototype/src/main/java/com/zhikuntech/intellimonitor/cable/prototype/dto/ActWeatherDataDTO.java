package com.zhikuntech.intellimonitor.cable.prototype.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 实时气象数据
 *
 * @author liukai
 */
@Data
public class ActWeatherDataDTO {

    /**
     * 风速
     */
    private BigDecimal windSpeed;

    /**
     * 高层
     */
    private BigDecimal highLevel;

    /**
     * 风向
     */
    private BigDecimal windDirection;

    /**
     * 温度
     */
    private BigDecimal temperature;

    /**
     * 湿度
     */
    private BigDecimal humidity;

    /**
     * 气压
     */
    private BigDecimal pressure;
}
