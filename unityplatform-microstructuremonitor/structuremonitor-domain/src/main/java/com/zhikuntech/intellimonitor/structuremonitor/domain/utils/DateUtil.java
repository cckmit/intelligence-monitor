package com.zhikuntech.intellimonitor.structuremonitor.domain.utils;

import com.zhikuntech.intellimonitor.core.commons.constant.StructureConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author： DAI
 * @date： Created in 2021/7/12 14:28
 */
@Slf4j
public class DateUtil {

    /**
     * @param stringDate 字符串日期
     * @param pattern    格式
     * @return : java.util.Date
     * @describe: 日期字符串转换成日期对象
     */
    public static Date string2Date(String stringDate, String pattern) {
        if (pattern == null || "".equals(pattern.trim())) {
            pattern = StructureConstant.DEFAULT_PATTERN;
        }
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(stringDate);
        } catch (Exception e) {
            log.error("#string2Date 时间转换异常", e);
        }
        return date;
    }

    /**
     * @param date 时间
     * @return : java.util.Date
     * @describe: 得到本月最后时刻
     */
    public static Date getDateByLastDay(Date date) {
        try {
            if (date == null) {
                return null;
            }
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime localTime = LocalTime.of(23, 59, 59);
            LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
            date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            return date;
        } catch (Exception e) {
            log.error("#getDateDayOfMonth 时间转换异常", e);
        }
        return null;
    }

}
