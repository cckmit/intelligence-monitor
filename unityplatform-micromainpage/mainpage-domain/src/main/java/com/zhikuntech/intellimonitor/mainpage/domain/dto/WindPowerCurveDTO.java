package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 11:53
 * @Description 风功率曲线
 * @Version 1.0
 */
@Data
public class WindPowerCurveDTO {
    /**
     * 短期预测功率
     */
    private List<TimePowerDTO> shortTermForecastPower;

    /**
     * 超短期预测功率
     */
    private List<TimePowerDTO>  supShortTermForecastPower;

    /**
     * 实际功率
     */
    private List<TimePowerDTO> actualPower;

    /**
     * 天气预报风速
     */
    private List<TimeWindSpeedDTO> weatherForecastPower;

    /**
     * 实测风速
     */
    private List<TimeWindSpeedDTO> measuredWindSpeed;

    /**
     * 实测温度
     */
    private BigDecimal cfTemperature;
}
