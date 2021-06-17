package com.zhikuntech.intellimonitor.mainpage.domain.service;

import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimePowerVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimeWindSpeedVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 17:57
 * @Description ${description}
 * @Version 1.0
 */
@Service
public interface WinPowerCurveService {
    /**
     * 获取时间段内的【风功率曲线】数据
     * @return
     */
    //WindPowerCurveVO getWindPowerCurveOfTimePeriod();

    /**
     * 获取所有时间内的【风功率曲线】数据
     * @return
     */
    WindPowerCurveVO getWindPowerCurveOfAllTime();

    /**
     * 获取庚顿数据库中当前【风功率曲线】数据
     * @return
     */
    WindPowerCurveVO getWindPowerCurve();

    /**
     * 订阅【风功率曲线】相关标签点快照改变的通知
     * @return 订阅结果
     */
    boolean subscribeWindPowerCurve(String username);

    /**
     * 获取短期预测功率
     * @return
     */
    List<TimePowerVO> getShortTermForecastPower();

    /**
     * 获取超短期预测功率
     * @return
     */
    List<TimePowerVO> getSupShortTermForecastPower();

    /**
     * 获取实际功率
     * @return
     */
    List<TimePowerVO> getActualPower();

    /**
     * 获取天气预报风速
     * @return
     */
    List<TimeWindSpeedVO> getWeatherForecastPower();

    /**
     * 获取实测风速
     * @return
     */
    List<TimeWindSpeedVO> getMeasuredWindSpeed();
}
