package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author liukai
 */
public class TimeProcessUtils {

    private static final String PATTERN_DATE_0 = "yyyy-MM-dd";

    private static final String PATTERN_0 = "yyyy-MM-dd HH:mm:ss";

    private static final String PATTERN_1 = "yyyy-MM-dd HH:mm";

    static DateTimeFormatter FORMAT_0 = DateTimeFormatter.ofPattern(PATTERN_0);

    static DateTimeFormatter FORMAT_1 = DateTimeFormatter.ofPattern(PATTERN_1);



    public static long localDateTimeToMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String formatLocalDateTimeWithSecondPattern(LocalDateTime localDateTime) {
        return DateFormatUtils.format(localDateTimeToMilli(localDateTime), PATTERN_0);
    }

    public static String formatLocalDateTimeWithMinutePattern(LocalDateTime localDateTime) {
        return DateFormatUtils.format(localDateTimeToMilli(localDateTime), PATTERN_1);
    }

    public static LocalDateTime parseLocalDateTimeWithSecondPattern(String dateStr) {
        return LocalDateTime.parse(dateStr, FORMAT_0);
    }

    public static LocalDateTime parseLocalDateTimeWithMinutePattern(String dateStr) {
        return LocalDateTime.parse(dateStr, FORMAT_1);
    }

    public static LocalDateTime parseHeaderByPatternOrExcept(String dtStr) {
        if (StringUtils.isBlank(dtStr)) {
            throw new IllegalArgumentException("时间参数异常:[" + dtStr + "]");
        }
        dtStr = StringUtils.trim(dtStr);
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(dtStr, FORMAT_0);
        } catch (Exception ex) {
            try {
                localDateTime = LocalDateTime.parse(dtStr, FORMAT_1);
            } catch (Exception iex) {
                // todo -> now do nothing
            }
        }
        if (Objects.isNull(localDateTime)) {
            throw new IllegalArgumentException("时间参数异常:[" + dtStr + "]");
        }
        return localDateTime;
    }

}
