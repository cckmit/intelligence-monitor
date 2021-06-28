package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.TimePowerDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.TimeWindSpeedDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.WindPowerCurveDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.schedule.WindPowerCurveSchedule;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimePowerVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.TimeWindSpeedVO;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import com.zhikuntech.intellimonitor.windpowerforecast.facade.ForeCastCurveFacade;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.dto.NwpListPatternDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.query.NwpCurvePatternQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 17:57
 * @Description 风功率曲线
 * @Version 1.0
 */
@Service
@Slf4j
public class WinPowerCurveServiceImpl implements WinPowerCurveService {
    @Autowired
    private ForeCastCurveFacade foreCastCurveFacade;

    @Override
    public WindPowerCurveVO getWindPowerCurveOfAllTimeOld() {
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
            if (shortTermForecastPowerList.get(shortTermForecastPowerIndex).getDate().after(nowDate)) {
                break;
            } else {
                shortTermForecastPowerIndex++;
            }
        }
        List<TimePowerVO> subShortTermForecastPowerList = shortTermForecastPowerList.subList(0, shortTermForecastPowerIndex);
        windPowerCurveVO.setShortTermForecastPower(subShortTermForecastPowerList);
        //超短期功率
        List<TimePowerVO> supShortTermForecastPowerList = WindPowerCurveSchedule.supShortTermForecastPowerList;
        int supShortTermForecastPowerIndex = 0;
        while (supShortTermForecastPowerIndex < supShortTermForecastPowerList.size()) {
            if (supShortTermForecastPowerList.get(supShortTermForecastPowerIndex).getDate().after(nowDate)) {
                break;
            } else {
                supShortTermForecastPowerIndex++;
            }
        }
        List<TimePowerVO> subSupShortTermForecastPowerList = supShortTermForecastPowerList.subList(0, supShortTermForecastPowerIndex);
        windPowerCurveVO.setSupShortTermForecastPower(subSupShortTermForecastPowerList);
        //实际功率
        List<TimePowerVO> actualPowerList = WindPowerCurveSchedule.actualPowerList;
        int actualPowerIndex = 0;
        while (actualPowerIndex < actualPowerList.size()) {
            if (actualPowerList.get(actualPowerIndex).getDate().after(nowDate)) {
                break;
            } else {
                actualPowerIndex++;
            }
        }
        List<TimePowerVO> subActualPowerList = actualPowerList.subList(0, actualPowerIndex);
        windPowerCurveVO.setActualPower(subActualPowerList);
        //预测气象
        List<TimeWindSpeedVO> weatherForecastPowerList = WindPowerCurveSchedule.weatherForecastPowerList;
        int weatherForecastPowerIndex = 0;
        while (weatherForecastPowerIndex < weatherForecastPowerList.size()) {
            if (weatherForecastPowerList.get(weatherForecastPowerIndex).getDate().after(nowDate)) {
                break;
            } else {
                weatherForecastPowerIndex++;
            }
        }
        List<TimeWindSpeedVO> subWeatherForecastPowerList = weatherForecastPowerList.subList(0, weatherForecastPowerIndex);
        windPowerCurveVO.setWeatherForecastPower(subWeatherForecastPowerList);
        //实际气象
        List<TimeWindSpeedVO> measuredWindSpeedList = WindPowerCurveSchedule.measuredWindSpeedList;
        int measuredWindSpeedIndex = 0;
        while (measuredWindSpeedIndex < measuredWindSpeedList.size()) {
            if (measuredWindSpeedList.get(measuredWindSpeedIndex).getDate().after(nowDate)) {
                break;
            } else {
                measuredWindSpeedIndex++;
            }
        }
        List<TimeWindSpeedVO> submeasuredWindSpeedList = measuredWindSpeedList.subList(0, measuredWindSpeedIndex);
        windPowerCurveVO.setMeasuredWindSpeed(submeasuredWindSpeedList);
        return windPowerCurveVO;
    }

    @Override
    public WindPowerCurveDTO getWindPowerCurveOfAllTime(NwpCurvePatternQuery nwpCurvePatternQuery) {
        BaseResponse<List<NwpListPatternDTO>> listBaseResponse = foreCastCurveFacade.nwpCurveQuery(nwpCurvePatternQuery);
        List<NwpListPatternDTO> listBaseResponseData = listBaseResponse.getData();
        log.info("接口响应数据->{}", listBaseResponseData);
        WindPowerCurveDTO windPowerCurveDTO = parseResult(listBaseResponseData);
        log.info("解析结果: windPowerCurveDTO->{}", windPowerCurveDTO);
        return windPowerCurveDTO;
    }

    /**
     * 解析接口响应数据
     *
     * @param nwpListPatternDTOList
     * @return
     */
    private WindPowerCurveDTO parseResult(List<NwpListPatternDTO> nwpListPatternDTOList) {
        WindPowerCurveDTO windPowerCurveDTO = new WindPowerCurveDTO();

        List<TimePowerDTO> shortTermForecastPower = new ArrayList<>();
        List<TimePowerDTO> supShortTermForecastPower = new ArrayList<>();
        List<TimePowerDTO> actualPower = new ArrayList<>();
        List<TimeWindSpeedDTO> weatherForecastPower = new ArrayList<>();
        List<TimeWindSpeedDTO> measuredWindSpeed = new ArrayList<>();

        windPowerCurveDTO.setShortTermForecastPower(shortTermForecastPower);
        windPowerCurveDTO.setSupShortTermForecastPower(supShortTermForecastPower);
        windPowerCurveDTO.setActualPower(actualPower);
        windPowerCurveDTO.setWeatherForecastPower(weatherForecastPower);
        windPowerCurveDTO.setMeasuredWindSpeed(measuredWindSpeed);

        //循环次数
        int index = 0;
        for (NwpListPatternDTO nwpListPatternDTO : nwpListPatternDTOList) {
            TimePowerDTO shortTermForecastPowerSingle = new TimePowerDTO();
            TimePowerDTO supShortTermForecastPowerSingle = new TimePowerDTO();
            TimePowerDTO actualPowerSingle = new TimePowerDTO();
            TimeWindSpeedDTO weatherForecastPowerSingle = new TimeWindSpeedDTO();
            TimeWindSpeedDTO measuredWindSpeedSingle = new TimeWindSpeedDTO();

            LocalDateTime dateTime = nwpListPatternDTO.getDateTime();
            shortTermForecastPowerSingle.setDate(dateTime);
            supShortTermForecastPowerSingle.setDate(dateTime);
            actualPowerSingle.setDate(dateTime);
            weatherForecastPowerSingle.setDate(dateTime);
            measuredWindSpeedSingle.setDate(dateTime);

            //短期预测功率
            BigDecimal dqPower = nwpListPatternDTO.getDqPower();
            shortTermForecastPowerSingle.setPower(dqPower);
            //超短期预测功率
            BigDecimal cdqPower = nwpListPatternDTO.getCdqPower();
            supShortTermForecastPowerSingle.setPower(cdqPower);
            //实际功率
            BigDecimal actPower = nwpListPatternDTO.getActPower();
            actualPowerSingle.setPower(actPower);
            //预测-风速
            BigDecimal nwpWindSpeed = nwpListPatternDTO.getNwpWindSpeed();
            weatherForecastPowerSingle.setSpeedTime(nwpWindSpeed);
            //实测风速
            BigDecimal cfWindSpeed = nwpListPatternDTO.getCfWindSpeed();
            measuredWindSpeedSingle.setSpeedTime(cfWindSpeed);
            //实测温度
            BigDecimal temperature = nwpListPatternDTO.getCfTemperature();
            if (index == nwpListPatternDTOList.size() - 1) {
                windPowerCurveDTO.setCfTemperature(temperature);
            }

            //加入容器
            shortTermForecastPower.add(shortTermForecastPowerSingle);
            supShortTermForecastPower.add(supShortTermForecastPowerSingle);
            actualPower.add(actualPowerSingle);
            weatherForecastPower.add(weatherForecastPowerSingle);
            measuredWindSpeed.add(measuredWindSpeedSingle);
            index++;
        }

        return windPowerCurveDTO;
    }

}
