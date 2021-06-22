package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimePowerVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimeWindSpeedVO;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/21 19:52
 * @Description 程序启动时给风功率曲线添加数据,把所有的数据一次性添加进去,每次调接口取数据时,把当前时刻之前的数据返回
 * @Version 1.0
 */
@Component
public class InitWindPowerCurve implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //一天时间换算成分钟
        int minuteIntervalOfOneDay = 24 * 60;

        //短期预测功率
        int shortTermForecastPowerInterval = 15;
        //超短期预测功率
        int supShortTermForecastPowerInterval = 15;
        //实际功率
        int actualPowerInterval = 5;
        //实际气象
        int measuredWindSpeedInterval = 1;

        //短期预测功率
        int shortTermForecastPowerCount = minuteIntervalOfOneDay / shortTermForecastPowerInterval;
        for(int i = 0;i < shortTermForecastPowerCount;i++){
            TimePowerVO timePower = WindPowerCurveSchedule.getTimePower(generateTime(i,shortTermForecastPowerInterval));
            WindPowerCurveSchedule.shortTermForecastPowerList.add(timePower);
        }

        //超短期预测功率
        int supShortTermForecastPowerCount = minuteIntervalOfOneDay / supShortTermForecastPowerInterval;
        for(int i = 0;i < supShortTermForecastPowerCount;i++){
            TimePowerVO timePower = WindPowerCurveSchedule.getTimePower(generateTime(i,supShortTermForecastPowerInterval));
            WindPowerCurveSchedule.supShortTermForecastPowerList.add(timePower);
        }

        //实际功率
        int actualPowerCount = minuteIntervalOfOneDay / actualPowerInterval;
        for(int i = 0;i < actualPowerCount;i++){
            TimePowerVO timePower = WindPowerCurveSchedule.getTimePower(generateTime(i,actualPowerInterval));
            WindPowerCurveSchedule.actualPowerList.add(timePower);
        }

        //实际气象
        int measuredWindSpeedCount = minuteIntervalOfOneDay / measuredWindSpeedInterval;
        for(int i = 0;i < measuredWindSpeedCount;i++){
            TimeWindSpeedVO timeWindSpeed = WindPowerCurveSchedule.getTimeWindSpeed(generateTime(i,measuredWindSpeedInterval),
                                                                            15, 20);
            WindPowerCurveSchedule.measuredWindSpeedList.add(timeWindSpeed);
        }

        //预测气象
        TimeWindSpeedVO firstTimeWindSpeed = WindPowerCurveSchedule.getTimeWindSpeed(new Date(8 * 60 * 60 * 1000 - 8 * 60 * 60 *1000),
                15, 20);
        TimeWindSpeedVO secondTimeWindSpeed = WindPowerCurveSchedule.getTimeWindSpeed(new Date(17 * 60 * 60 * 1000 - 8 * 60 * 60 *1000),
                15, 20);
        WindPowerCurveSchedule.weatherForecastPowerList.add(firstTimeWindSpeed);
        WindPowerCurveSchedule.weatherForecastPowerList.add(secondTimeWindSpeed);
    }

    private Date generateTime(int i,int interval){
        return new Date(interval * i * 1000 * 60 - 8 * 60 * 60 *1000);
    }
}
