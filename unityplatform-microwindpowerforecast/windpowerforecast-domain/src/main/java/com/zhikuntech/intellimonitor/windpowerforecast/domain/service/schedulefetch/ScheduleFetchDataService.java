package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.schedulefetch;

/**
 * <p>
 *     1.实际功率数据(5min)
 *     2.实际气象数据(1min)
 * </p>
 *
 * @author liukai
 */
public interface ScheduleFetchDataService {

    /**
     * 调度获取实际功率数据
     */
    void scheduleFetchActPower();

    /**
     * 调度获取实际气象数据
     */
    void scheduleFetchActWeather();

    /**
     * 调度获取容量数据
     */
    void scheduleFetchCapacity();

}
