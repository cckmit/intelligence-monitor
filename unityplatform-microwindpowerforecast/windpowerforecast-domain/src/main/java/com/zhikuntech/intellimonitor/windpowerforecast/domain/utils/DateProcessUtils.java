package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * @author liukai
 */
@Slf4j
public class DateProcessUtils {

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseDateY4M2D2(String dateStr) {
        Date parse = null;
        try {
            dateStr = StringUtils.trim(dateStr);
            parse = FORMAT.parse(dateStr);
        } catch (Exception ex) {
            log.error("parse date[{}] occur error:[{}]", dateStr, ex.getMessage());
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

}
