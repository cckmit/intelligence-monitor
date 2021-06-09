package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.rtdb.api.callbackInter.RSDataChange;
import com.rtdb.api.callbackInter.RSDataChangeEx;
import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.mainpage.domain.base.ResultCode;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.GetSnapshotsException;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.SubscribeGoldenException;
import com.zhikuntech.intellimonitor.mainpage.domain.exception.UserNotLoginException;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimePowerVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimeWindSpeedVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    GoldenUtil goldenUtil = new GoldenUtil();

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public WindPowerCurveVO getWindPowerCurve(){
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
        List<TimePowerVO>  supShortTermForecastPower = new ArrayList<>();
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
    public boolean subscribeWindPowerCurve(String username){
        ConcurrentHashMap<String, WebSocketServer> clients = webSocketServer.getClients();
        int[] ids = new int[]{11,12,13,14,15};
        //判断用户是否连接
//        if (clients.containsKey(username)){
        if (true){
            //订阅
            try {
                goldenUtil.subscribeSnapshots(ids,new RSDataChange(){
                    @Override
                    public void run(RtdbData[] rtdbData) {
                        LOGGER.info("rtdbData=>"+Arrays.toString(rtdbData));  //数据

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
                        List<TimePowerVO>  supShortTermForecastPowerList = new ArrayList<>();
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

                        LOGGER.info("windPowerCurveVO=>{}",windPowerCurveVO);
                        webSocketServer.sendMessage(JSON.toJSONString(windPowerCurveVO),username);
                    }
                });
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new SubscribeGoldenException(ResultCode.GOLDEN_SUBSCRIBESNAPSHOTS_FAILED);
            }
        }else {
            throw new UserNotLoginException(ResultCode.USER_NOT_LOGIN_EXCEPTION);
        }
    }
}
