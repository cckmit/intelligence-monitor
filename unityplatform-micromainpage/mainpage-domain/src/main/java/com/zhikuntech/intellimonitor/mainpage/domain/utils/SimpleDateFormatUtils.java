package com.zhikuntech.intellimonitor.mainpage.domain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 格式化日期的工具类
 *
 * @author
 */
public class SimpleDateFormatUtils {
    /**
     * yyyy-MM-dd HH:mm:ss 类型的日期 时间格式
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

    /**
     * yyyy-MM-dd类型的日期格式
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 2020/05
     */
    public static final String yyyy_MM =  "yyyy/MM";
    /**
     * 根据输入的format格式，以及format字符串，返回对应的日期
     *
     * @param pattern，字符串的format格式，例如：yyyy-MM-dd        HH:mm:ss
     * @param dateFormatStr，format后的日期字符串，例如：2015-02-10 22:00:00
     * @return java.util.Date对象
     * @throws ParseException
     */
    public static Date getDataByFormatString(String pattern, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateFormatStr);
    }

    public static boolean isValidDate(String str,String pattern) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public static void main(String[] args) {
        boolean date = isValidDate("2020/06", "yyyy/MM");
        System.out.println(date);
    }

    /**
     * @param date,需要转换为指定格式的日期对象
     * @return
     */
    public static String getFormatStrByPatternAndDate( Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return simpleDateFormat.format(date);
    }



    /**
     * 获取当前时间月份的第n天
     *
     * @param time
     * @return
     */
    public static String getMonthDay(Date time , Integer n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, n);
        return df.format(gcLast.getTime());
    }


}
