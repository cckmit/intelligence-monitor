package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import com.zhikuntech.intellimonitor.mainpage.domain.golden.annotation.GoldenId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 实时气象数据
 *
 * @author liukai
 */
@Data
public class ActWeatherDataVO {

    /**
     * 风速
     */
    @GoldenId(1)
    private Double windSpeed;

    /**
     * 高层
     */
    @GoldenId(2)
    private Double highLevel;

    /**
     * 风向
     */
    @GoldenId(3)
    private Double windDirection;

    /**
     * 温度
     */
    @GoldenId(4)
    private Double temperature;

    /**
     * 湿度
     */
    @GoldenId(5)
    private Double humidity;

    /**
     * 气压
     */
    @GoldenId(6)
    private Double pressure;
}
