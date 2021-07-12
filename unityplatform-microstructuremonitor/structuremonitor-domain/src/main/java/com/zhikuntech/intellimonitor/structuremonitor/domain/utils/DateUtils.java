package com.zhikuntech.intellimonitor.structuremonitor.domain.utils;

import com.zhikuntech.intellimonitor.core.commons.utils.SFTPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * @author： DAI
 * @date： Created in 2021/7/12 14:28
 */
public class DateUtils {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    /**
     * @param stringDate 字符串日期
     * @param pattern    格式
     * @return : java.util.Date
     * @describe: 日期字符串转换成LocalDateTime
     */
    public static LocalDateTime string2LocalDateTime(String stringDate, String pattern) {

        if (StringUtils.isBlank(pattern)) {
            pattern = DEFAULT_PATTERN;
        }
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("日期转换失败，错误信息：{}", e.getMessage());
        }
        if (Objects.nonNull(date)) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }

    /**
     * @param stringDate 字符串日期
     * @param pattern    格式
     * @return : java.util.Date
     * @date: Creat in 2021/6/23 16:26
     * @describe: 日期字符串转换成日期对象
     */
    public static Date string2Date(String stringDate, String pattern) {
        if (pattern == null || "".equals(pattern.trim())) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

}
