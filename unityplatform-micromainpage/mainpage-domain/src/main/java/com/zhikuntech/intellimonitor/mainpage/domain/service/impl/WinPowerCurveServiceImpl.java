package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.rtdb.api.callbackInter.RSDataChange;
import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
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

import javax.websocket.Session;
import java.text.NumberFormat;
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
        //1:调用远程接口查询获取....
        //先模拟数据
        WindPowerCurveVO windPowerCurveVO = new WindPowerCurveVO();
        int count = 24 * 4;
        Random random = new Random();
        Double d = random.nextDouble() * 100 + 20;
        //指定范围
        //保留位数
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        List<TimePowerVO> shortTermForecastPowerList = new ArrayList<>();
        List<TimePowerVO> supShortTermForecastPowerList = new ArrayList<>();
        List<TimePowerVO> actualPowerList = new ArrayList<>();
        List<TimeWindSpeedVO> weatherForecastPowerList = new ArrayList<>();
        List<TimeWindSpeedVO> measuredWindSpeedList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            //减去8小时时区时间
            Date date = new Date(15 * (i + 1) * 60 * 1000 - 8 * 60 * 60 *1000);
            TimePowerVO shortTermForecastPower = new TimePowerVO();
            shortTermForecastPower.setDate(date);
            shortTermForecastPower.setPower(Double.parseDouble(nf.format(random.nextDouble() * 100 + 20)));

            TimePowerVO supShortTermForecastPower = new TimePowerVO();
            supShortTermForecastPower.setDate(date);
            supShortTermForecastPower.setPower(Double.parseDouble(nf.format(random.nextDouble() * 100 + 20)));

            TimePowerVO actualPower = new TimePowerVO();
            actualPower.setDate(date);
            actualPower.setPower(Double.parseDouble(nf.format(random.nextDouble() * 100 + 20)));

            TimeWindSpeedVO weatherForecastPower = new TimeWindSpeedVO();
            weatherForecastPower.setDate(date);
            weatherForecastPower.setSpeedTime(Double.parseDouble(nf.format(random.nextDouble() * 100 + 20)));

            TimeWindSpeedVO measuredWindSpeed = new TimeWindSpeedVO();
            measuredWindSpeed.setDate(date);
            measuredWindSpeed.setSpeedTime(Double.parseDouble(nf.format(random.nextDouble() * 100 + 20)));

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
        }
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
        if (clients.containsKey(username)){
            //订阅
            try {
                goldenUtil.subscribeSnapshots(clientId,ids, new RSDataChange() {
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
}
