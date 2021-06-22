package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimePowerVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimeWindSpeedVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Author 杨锦程
 * @Date 2021/6/21 19:20
 * @Description 风功率曲线定时任务
 * @Version 1.0
 */
@Component
@Slf4j
public class WindPowerCurveSchedule {
    public static List<TimePowerVO> shortTermForecastPowerList = new ArrayList<>();
    public static List<TimePowerVO> supShortTermForecastPowerList = new ArrayList<>();
    public static List<TimePowerVO> actualPowerList = new ArrayList<>();
    public static List<TimeWindSpeedVO> weatherForecastPowerList = new ArrayList<>();
    public static List<TimeWindSpeedVO> measuredWindSpeedList = new ArrayList<>();

    private static Random random = new Random();
    //指定范围保留位数
    private static NumberFormat nf = NumberFormat.getNumberInstance();

    /**
     * 获取时间功率
     * @param date
     * @return
     */
    public static TimePowerVO getTimePower(Date date){
        //指定范围保留位数
        nf.setMaximumFractionDigits(2);
        TimePowerVO timePowerVO = new TimePowerVO();
        timePowerVO.setDate(date);
        timePowerVO.setPower(Double.parseDouble(nf.format(random.nextDouble() * 100 + 20)));
        return timePowerVO;
    }

    /**
     * 获取时间风速
     * @param date
     * @return
     */
    public static TimeWindSpeedVO getTimeWindSpeed(Date date,int multiply,int plus){
        //指定范围保留位数
        nf.setMaximumFractionDigits(2);
        TimeWindSpeedVO timeWindSpeedVO = new TimeWindSpeedVO();
        timeWindSpeedVO.setDate(date);
        timeWindSpeedVO.setSpeedTime(Double.parseDouble(nf.format(random.nextDouble() * multiply + plus)));
        return timeWindSpeedVO;
    }

    /**
     * 超短期预测功率
     * 每15分钟生成一条数据
     */
    //@Scheduled(cron = "0 */15 * * * ?")
    public void supShortTermForecastPower(){
        TimePowerVO timePower = getTimePower(new Date());
        log.info("定时器生成[超短期预测功率]->{}",timePower);
        supShortTermForecastPowerList.add(timePower);
    }

    /**
     * 实际功率
     * 每5分钟生成一条数据
     */
    //@Scheduled(cron = "0 */5 * * * ?")
    public void actualPower(){
        TimePowerVO timePower = getTimePower(new Date());
        log.info("定时器生成[实际功率]->{}",timePower);
        actualPowerList.add(timePower);
    }

    /**
     * 天气预报是一天刷新两次,分别在早8点前和下午5-6点
     */
    //@Scheduled(cron = "0 0 8,17 * * ?")
    public void weatherForecastPower(){
        TimeWindSpeedVO timeWindSpeed = getTimeWindSpeed(new Date(), 15, 20);
        log.info("定时器生成[气象预测]->{}",timeWindSpeed);
        weatherForecastPowerList.add(timeWindSpeed);
    }

    /**
     * 实际气象
     * 每分钟刷新一次
     */
    //@Scheduled(cron = "0 */1 * * * ?")
    public void measuredWindSpeed(){
        TimeWindSpeedVO timeWindSpeed = getTimeWindSpeed(new Date(), 15, 20);
        log.info("定时器生成[实际气象]->{}",timeWindSpeed);
        measuredWindSpeedList.add(timeWindSpeed);
    }
}
