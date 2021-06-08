package com.zhikuntech.intellimonitor.mainpage.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 11:53
 * @Description 风功率曲线
 * @Version 1.0
 */
@Data
public class WindPowerCurveVO {
    /**
     * 短期预测功率
     */
    private List<TimePowerVO> shortTermForecastPower;

    /**
     * 超短期预测功率
     */
    private List<TimePowerVO>  supShortTermForecastPower;

    /**
     * 实际功率
     */
    private List<TimePowerVO> actualPower;

    /**
     * 天气预报风速
     */
    private List<TimeWindSpeedVO> weatherForecastPower;

    /**
     * 实测风速
     */
    private List<TimeWindSpeedVO> measuredWindSpeed;
}
