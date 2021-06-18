package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.DqDayElectricGenDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * @author liukai
 */
public class CalcUtils {


    public static BigDecimal calcPower(BigDecimal pressure, BigDecimal windSpeed) {
        /*
            功率=1/2 X 空气密度[压力]  X（速度）^3 （w）
              其中：
              功率单位为瓦特；
        　　空气密度单位为千克/立方米；
        　　面积指气流横截面积，单位为平方米；
        　　速度单位为米/秒
         */
        if (Objects.nonNull(pressure) && Objects.nonNull(windSpeed)) {
            double calcPower = 0.5 * pressure.doubleValue() * Math.pow(windSpeed.doubleValue(), 3);
            return new BigDecimal(calcPower).setScale(3, RoundingMode.HALF_EVEN);
        }
        return new BigDecimal("0");
    }

    /**
     * 计算日发电量
     * @param l 单日下的发电数据
     * @return  当日预测发电量
     */
    public static BigDecimal calcFutureDayElectricGen(List<DqDayElectricGenDTO> l) {
        BigDecimal multi = new BigDecimal("0.125");

        BigDecimal calc = new BigDecimal("0");
        int size = l.size();
        for (int i = 0; i < size - 1; i++) {
            BigDecimal genPre = l.get(i).getElectricGen();
            BigDecimal genPost = l.get(i + 1).getElectricGen();
            calc = calc.add(genPre).add(genPost);
        }
        calc = calc.multiply(multi).setScale(3, BigDecimal.ROUND_HALF_EVEN);
        return calc;
    }
}
