package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.rtdb.api.callbackInter.RSDataChange;
import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.GetSnapshotsException;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.SubscribeGoldenException;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.UserNotLoginException;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.schedule.InitWindPowerCurve;
import com.zhikuntech.intellimonitor.mainpage.domain.schedule.WindPowerCurveSchedule;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimePowerVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimeWindSpeedVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 17:57
 * @Description 风功率曲线
 * @Version 1.0
 */
@Service
public class WinPowerCurveServiceImpl implements WinPowerCurveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WinPowerCurveServiceImpl.class);

    @Autowired
    private GoldenUtil goldenUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public WindPowerCurveVO getWindPowerCurveOfAllTime() {
        //调用远程接口查询获取....
        //先模拟数据
        WindPowerCurveVO windPowerCurveVO = new WindPowerCurveVO();
        //1:获取当前时间的时分秒部分,得到一个新的Date时间对象
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        int second = LocalDateTime.now().getSecond();
        Date nowDate = new Date((hour * 60 * 60 + minute * 60 + second) * 1000 - 8 * 60 * 60 * 1000);
        //2:使用这个新的Date对象去各曲线集合中比较,返回小于当前时间的所有数据
        //短期功率
        List<TimePowerVO> shortTermForecastPowerList = WindPowerCurveSchedule.shortTermForecastPowerList;
        int shortTermForecastPowerIndex = 0;
        while (shortTermForecastPowerIndex < shortTermForecastPowerList.size()) {
            if(shortTermForecastPowerList.get(shortTermForecastPowerIndex).getDate().after(nowDate)){
                break;
            }else {
                shortTermForecastPowerIndex++;
            }
        }
        List<TimePowerVO> subShortTermForecastPowerList = shortTermForecastPowerList.subList(0, shortTermForecastPowerIndex);
        windPowerCurveVO.setShortTermForecastPower(subShortTermForecastPowerList);
        //超短期功率
        List<TimePowerVO> supShortTermForecastPowerList = WindPowerCurveSchedule.supShortTermForecastPowerList;
        int supShortTermForecastPowerIndex = 0;
        while (supShortTermForecastPowerIndex < supShortTermForecastPowerList.size()){
            if(supShortTermForecastPowerList.get(supShortTermForecastPowerIndex).getDate().after(nowDate)){
                break;
            }else {
                supShortTermForecastPowerIndex++;
            }
        }
        List<TimePowerVO> subSupShortTermForecastPowerList = supShortTermForecastPowerList.subList(0, supShortTermForecastPowerIndex);
        windPowerCurveVO.setSupShortTermForecastPower(subSupShortTermForecastPowerList);
        //实际功率
        List<TimePowerVO> actualPowerList = WindPowerCurveSchedule.actualPowerList;
        int actualPowerIndex = 0;
        while (actualPowerIndex < actualPowerList.size()){
            if(actualPowerList.get(actualPowerIndex).getDate().after(nowDate)){
                break;
            }else {
                actualPowerIndex++;
            }
        }
        List<TimePowerVO> subActualPowerList = actualPowerList.subList(0, actualPowerIndex);
        windPowerCurveVO.setActualPower(subActualPowerList);
        //预测气象
        List<TimeWindSpeedVO> weatherForecastPowerList = WindPowerCurveSchedule.weatherForecastPowerList;
        int weatherForecastPowerIndex = 0;
        while (weatherForecastPowerIndex < weatherForecastPowerList.size()){
            if(weatherForecastPowerList.get(weatherForecastPowerIndex).getDate().after(nowDate)){
                break;
            }else {
                weatherForecastPowerIndex++;
            }
        }
        List<TimeWindSpeedVO> subWeatherForecastPowerList = weatherForecastPowerList.subList(0, weatherForecastPowerIndex);
        windPowerCurveVO.setWeatherForecastPower(subWeatherForecastPowerList);
        //实际气象
        List<TimeWindSpeedVO> measuredWindSpeedList = WindPowerCurveSchedule.measuredWindSpeedList;
        int measuredWindSpeedIndex = 0;
        while (measuredWindSpeedIndex < measuredWindSpeedList.size()){
            if(measuredWindSpeedList.get(measuredWindSpeedIndex).getDate().after(nowDate)){
                break;
            }else {
                measuredWindSpeedIndex++;
            }
        }
        List<TimeWindSpeedVO> submeasuredWindSpeedList = measuredWindSpeedList.subList(0, measuredWindSpeedIndex);
        windPowerCurveVO.setMeasuredWindSpeed(submeasuredWindSpeedList);
        return windPowerCurveVO;
    }

    @Override
    public WindPowerCurveVO getWindPowerCurve() {
        int[] ids = new int[]{1};
        try {
            goldenUtil.getSnapshots(ids);  //此处异常统一处理
        } catch (Exception e) {
            throw new GetSnapshotsException(ResultCode.GOLDEN_GETSNAPSHOTS_FAILED);
        }
        WindPowerCurveVO windPowerCurveVO = new WindPowerCurveVO();
        TimePowerVO timePowerVO = new TimePowerVO();
        timePowerVO.setDate(new Date());
        timePowerVO.setPower(283.23);
        TimeWindSpeedVO timeWindSpeedVO = new TimeWindSpeedVO();
        timeWindSpeedVO.setDate(new Date());
        timeWindSpeedVO.setSpeedTime(421.234);
        List<TimePowerVO> shortTermForecastPower = new ArrayList<>();
        List<TimePowerVO> supShortTermForecastPower = new ArrayList<>();
        List<TimePowerVO> actualPower = new ArrayList<>();
        List<TimeWindSpeedVO> weatherForecastPower = new ArrayList<>();
        List<TimeWindSpeedVO> measuredWindSpeed = new ArrayList<>();
        shortTermForecastPower.add(timePowerVO);
        supShortTermForecastPower.add(timePowerVO);
        actualPower.add(timePowerVO);
        weatherForecastPower.add(timeWindSpeedVO);
        measuredWindSpeed.add(timeWindSpeedVO);
        //添加数据
        windPowerCurveVO.setActualPower(actualPower);
        windPowerCurveVO.setMeasuredWindSpeed(measuredWindSpeed);
        windPowerCurveVO.setShortTermForecastPower(shortTermForecastPower);
        windPowerCurveVO.setSupShortTermForecastPower(supShortTermForecastPower);
        windPowerCurveVO.setWeatherForecastPower(weatherForecastPower);
        return windPowerCurveVO;
    }

    @Override
    public boolean subscribeWindPowerCurve(String username) {
        ConcurrentHashMap<String, Session> clients = WebSocketServer.clients;
        String clientId = "";  //客户端标识
        int[] ids = new int[]{11, 12, 13, 14, 15};
        //判断用户是否连接
        if (clients.containsKey(username)) {
            //订阅
            try {
                goldenUtil.subscribeSnapshots(clientId, ids, new RSDataChange() {
                    @Override
                    public void run(RtdbData[] rtdbData) {
                        LOGGER.info("rtdbData=>" + Arrays.toString(rtdbData));  //数据

                        //websocket推送给客户端
                        WindPowerCurveVO windPowerCurveVO = new WindPowerCurveVO();
                        TimePowerVO shortTermForecastPower = new TimePowerVO();
                        shortTermForecastPower.setDate(rtdbData[0].getDate());
                        shortTermForecastPower.setPower((Double) rtdbData[0].getValue());

                        TimePowerVO supShortTermForecastPower = new TimePowerVO();
                        supShortTermForecastPower.setDate(rtdbData[1].getDate());
                        supShortTermForecastPower.setPower((Double) rtdbData[1].getValue());

                        TimePowerVO actualPower = new TimePowerVO();
                        actualPower.setDate(rtdbData[2].getDate());
                        actualPower.setPower((Double) rtdbData[2].getValue());

                        TimeWindSpeedVO weatherForecastPower = new TimeWindSpeedVO();
                        weatherForecastPower.setDate(rtdbData[3].getDate());
                        weatherForecastPower.setSpeedTime((Double) rtdbData[3].getValue());

                        TimeWindSpeedVO measuredWindSpeed = new TimeWindSpeedVO();
                        measuredWindSpeed.setDate(rtdbData[4].getDate());
                        measuredWindSpeed.setSpeedTime((Double) rtdbData[4].getValue());

                        List<TimePowerVO> shortTermForecastPowerList = new ArrayList<>();
                        List<TimePowerVO> supShortTermForecastPowerList = new ArrayList<>();
                        List<TimePowerVO> actualPowerList = new ArrayList<>();
                        List<TimeWindSpeedVO> weatherForecastPowerList = new ArrayList<>();
                        List<TimeWindSpeedVO> measuredWindSpeedList = new ArrayList<>();

                        shortTermForecastPowerList.add(shortTermForecastPower);
                        supShortTermForecastPowerList.add(supShortTermForecastPower);
                        actualPowerList.add(actualPower);
                        weatherForecastPowerList.add(weatherForecastPower);
                        measuredWindSpeedList.add(measuredWindSpeed);
                        //添加数据
                        windPowerCurveVO.setActualPower(actualPowerList);
                        windPowerCurveVO.setMeasuredWindSpeed(measuredWindSpeedList);
                        windPowerCurveVO.setShortTermForecastPower(shortTermForecastPowerList);
                        windPowerCurveVO.setSupShortTermForecastPower(supShortTermForecastPowerList);
                        windPowerCurveVO.setWeatherForecastPower(weatherForecastPowerList);

                        LOGGER.info("windPowerCurveVO=>{}", JSON.toJSONString(windPowerCurveVO));
                        webSocketServer.sendMessage(JSON.toJSONString(windPowerCurveVO), username);
                    }
                });
                return true;
            } catch (Exception e) {
                //e.printStackTrace();
                throw new SubscribeGoldenException(ResultCode.GOLDEN_SUBSCRIBESNAPSHOTS_FAILED);
            }
        } else {
            throw new UserNotLoginException(ResultCode.USER_NOT_LOGIN_EXCEPTION);
        }
    }


    @Override
    public List<TimePowerVO> getShortTermForecastPower() {
//        return getTimePower(new Date());
        return null;
    }

    @Override
    public List<TimePowerVO> getSupShortTermForecastPower() {
//        return getTimePower(new Date());
        return null;
    }

    @Override
    public List<TimePowerVO> getActualPower() {
//        return getTimePower(new Date());
        return null;
    }

    @Override
    public List<TimeWindSpeedVO> getWeatherForecastPower() {
//        return getTimeWindSpeed(new Date(),20,15);
        return null;
    }

    @Override
    public List<TimeWindSpeedVO> getMeasuredWindSpeed() {
        return null;
    }
}
