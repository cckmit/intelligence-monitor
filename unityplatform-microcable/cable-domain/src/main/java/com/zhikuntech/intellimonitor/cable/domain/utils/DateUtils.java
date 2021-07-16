package com.zhikuntech.intellimonitor.cable.domain.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    // 获取当前日期2个月前的时间
    public static String getTwoMonthBefore(String nowTime){

           // 获取当前时间
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
           //得到日历
           calendar.setTime(date);
           //把当前时间赋给日历
           calendar.add(Calendar.MONTH, -1);
           //设置为前2月，可根据需求进行修改
           date = calendar.getTime();
           //获取2个月前的时间
           return dateFormat.format(date);
    }
    //获取月的最后一天
    public static String getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();       //清空cal缓存，避免2月份取到的值不正确
        cal.clear();        //设置年份
        cal.set(Calendar.YEAR,year);//设置月份
        cal.set(Calendar.MONTH, month-1);        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /*** 获得该月第一天* @param year* @param month* @return*/
    public static String getFirstDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();        //清空cal缓存，避免2月份取到的值不正确
                cal.clear();        //设置年份
                cal.set(Calendar.YEAR,year);        //设置月份
                cal.set(Calendar.MONTH, month-1);        //获取某月最小天数
                int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);        //设置日历中月份的最小天数
                cal.set(Calendar.DAY_OF_MONTH, firstDay);        //格式化日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String firstDayOfMonth = sdf.format(cal.getTime());
                return firstDayOfMonth;
    }
    /** 获取前一个月 */
    public static  String getFirstMonth(Date date) throws ParseException {
        //获取开始时间的前一个月的时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1); // 设置为上一个月
        Date time = calendar.getTime();

        String startTime = format.format(time);
        return startTime;
    }

    /** * 字符串转换成日期 * @param str * @return date */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**  * 格式化时间 */
    public static String dateFormate(String pattern, Date date){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置北京时区
        return format.format(date);
    }
    /**
     * 时间戳转日期
     */
    public static Date TimeStamp2Date(long timestampString) {
        return new Date(timestampString * 1000);
    }

    /**
     * 获取当前时间指定格式下的字符串.
     *
     * @param pattern 转化后时间展示的格式，例如"yyyy-MM-dd"，"yyyy-MM-dd HH:mm:ss"等
     * @return String 格式转换之后的时间字符串.
     * @since 1.0
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 获取当前日期指定天数之前的日期.
     *
     * @param num 相隔天数
     * @return Date 日期
     * @since 1.0
     */
    public static String beforeDay(int num,String pattern) {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) - num);
        return DateFormatUtils.format(curr.getTime(),pattern);
    }

}
