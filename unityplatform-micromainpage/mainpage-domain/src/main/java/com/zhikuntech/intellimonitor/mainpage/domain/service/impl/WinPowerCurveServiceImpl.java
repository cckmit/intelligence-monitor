package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.TimePowerDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.TimeWindSpeedDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.WindPowerCurveDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.windpowerforecast.facade.ForeCastCurveFacade;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.dto.NwpListPatternDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.query.NwpCurvePatternQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private JsonMapper jsonMapper = new JsonMapper();

    @Override
    public WindPowerCurveDTO getWindPowerCurveOfAllTime(NwpCurvePatternQuery nwpCurvePatternQuery) {
        BaseResponse<List<NwpListPatternDTO>> listBaseResponse = foreCastCurveFacade.nwpCurveQuery(nwpCurvePatternQuery);
        List<NwpListPatternDTO> listBaseResponseData = listBaseResponse.getData();
        try {
            log.info("接口响应数据->{}", jsonMapper.writeValueAsString(listBaseResponseData));
        } catch (JsonProcessingException e) {
            log.warn("接口响应数据转为json异常");
        }
        WindPowerCurveDTO windPowerCurveDTO = parseResult(listBaseResponseData);
        try {
            log.info("解析结果: windPowerCurveDTO->{}", jsonMapper.writeValueAsString(windPowerCurveDTO));
        } catch (JsonProcessingException e) {
            log.warn("解析后数据转为json异常");
        }
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

        windPowerCurveDTO.setShortTermForecastPower(shortTermForecastPower);
        windPowerCurveDTO.setSupShortTermForecastPower(supShortTermForecastPower);
        windPowerCurveDTO.setActualPower(actualPower);
        windPowerCurveDTO.setWeatherForecastPower(weatherForecastPower);
        windPowerCurveDTO.setMeasuredWindSpeed(measuredWindSpeed);

        return windPowerCurveDTO;
    }

}
