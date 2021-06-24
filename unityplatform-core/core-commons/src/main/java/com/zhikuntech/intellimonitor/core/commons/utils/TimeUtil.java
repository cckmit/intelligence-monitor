package com.zhikuntech.intellimonitor.core.commons.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Author 杨锦程
 * @Date 2021/6/22 16:54
 * @Description 时间格式工具类
 * @Version 1.0
 */
public class TimeUtil {
    public static final String YEAR_MONTH_DAY_SECOND = "yyyy-MM-dd HH:mm:ss";

    private static ThreadLocal<Map<String, SimpleDateFormat>> sdfmThreadLocal = new ThreadLocal<>();

    private static SimpleDateFormat getDateFormat(String pattern){
        Map<String, SimpleDateFormat> stringSimpleDateFormatMap = sdfmThreadLocal.get();
        if(stringSimpleDateFormatMap == null){
            stringSimpleDateFormatMap = new HashMap<>();
        }
        SimpleDateFormat simpleDateFormat = stringSimpleDateFormatMap.get(pattern);
        if(simpleDateFormat == null){
            simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            stringSimpleDateFormatMap.put(pattern,simpleDateFormat);
            sdfmThreadLocal.set(stringSimpleDateFormatMap);
        }
        return simpleDateFormat;
    }

    /**
     * Date转换成String
     * @param date 要格式化的时间
     * @param pattern 要格式化的类型
     * @return
     */
    public static String formateDate(Date date,String pattern){
        if(date == null || pattern == null){
            return null;
        }
        return getDateFormat(pattern).format(date);
    }

    /**
     * String转换成Date
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) throws ParseException {
        if(!StringUtils.isEmpty(dateStr)){
            return getDateFormat(YEAR_MONTH_DAY_SECOND).parse(dateStr);
        }
        return null;
    }
}
