package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * @author liukai
 */
@Slf4j
public class DateProcessUtils {

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateTimeFormatter NORMAL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * {@code synchronized }
     * 关键字必须
     * @param dateStr   日期字符串
     * @return          {@link Date}
     */
    public static synchronized Date parseDateY4M2D2(String dateStr) {
        Date parse = null;
        try {
            dateStr = StringUtils.trim(dateStr);
            parse = FORMAT.parse(dateStr);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("parse date[{}] occur error:[{}]", dateStr, ex.getMessage());
            throw new IllegalArgumentException(ex);
        }
        return parse;
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime parseToLocalDateTime(String dateStr) {
        return dateToLocalDateTime(parseDateY4M2D2(dateStr));
    }

    public static LocalDate parseToLocalDate(String dateStr) {
        LocalDateTime localDateTime = dateToLocalDateTime(parseDateY4M2D2(dateStr));
        if (Objects.isNull(localDateTime)) {
             throw new IllegalArgumentException();
        }
        return localDateTime.toLocalDate();
    }

    public static String fetchDayBegin(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String fetchTomorrowBegin(LocalDate localDate) {
        return localDate.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String formatNormalDateY4M2D2(LocalDate localDate) {
        return localDate.format(NORMAL_FORMAT);
    }

}
