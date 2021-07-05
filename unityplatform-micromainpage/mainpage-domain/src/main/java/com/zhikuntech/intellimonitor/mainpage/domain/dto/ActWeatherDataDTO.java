package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
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
    @GoldenId(1)
    private BigDecimal windSpeed;

    /**
     * 高层
     */
    @GoldenId(2)
    private BigDecimal highLevel;

    /**
     * 风向
     */
    @GoldenId(3)
    private BigDecimal windDirection;

    /**
     * 温度
     */
    @GoldenId(4)
    private BigDecimal temperature;

    /**
     * 湿度
     */
    @GoldenId(5)
    private BigDecimal humidity;

    /**
     * 气压
     */
    @GoldenId(6)
    private BigDecimal pressure;
}
