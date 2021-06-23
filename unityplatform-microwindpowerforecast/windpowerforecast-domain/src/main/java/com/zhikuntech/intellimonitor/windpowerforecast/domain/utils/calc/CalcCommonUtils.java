package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author liukai
 */
public class CalcCommonUtils {


    public static Function<LocalDateTime, String> localDateTimeStringFunction = item -> {
        if (Objects.isNull(item)) {
            return "nil";
        }
        return TimeProcessUtils.formatLocalDateTimeWithSecondPattern(item);
    };


    /**
     * 时间向后落点
     * 如:
     * 00:11 -> 00:15
     * 00:12 -> 00:15
     * 00:15 -> 00:15
     */
    public static Function<LocalDateTime, String> timePostRangeProcess = item -> {
        if (Objects.nonNull(item)) {
            LocalDate date = item.toLocalDate();
            LocalTime time = item.toLocalTime();

            int minute = time.getMinute();
            //# 具体算法
            int tNum = minute / 15;
            int div = minute % 15;

            int willPlus = tNum * 15;
            if (div != 0) {
                willPlus = willPlus + 15;
            }
            if (minute == 0) {
                willPlus = 0;
            }
            //# 具体算法
            LocalDateTime calcDt = LocalDateTime.of(date, LocalTime.of(time.getHour(), 0, 0))
                    .plusMinutes(willPlus);
            return TimeProcessUtils.formatLocalDateTimeWithSecondPattern(calcDt);
        }
        return "nil";
    };
}
