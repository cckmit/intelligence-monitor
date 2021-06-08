package com.zhikuntech.intellimonitor.mainpage.domain.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LocalDateTimeUtils {

    //获取当前时间的LocalDateTime对象
    //LocalDateTime.now();

    //根据年月日构建LocalDateTime
    //LocalDateTime.of();

    //比较日期先后
    //LocalDateTime.now().isBefore(),
    //LocalDateTime.now().isAfter(),

    //Date转换为LocalDateTime
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    //LocalDateTime转换为Date
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    //获取指定日期的毫秒
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    //获取指定日期的秒
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    //获取指定时间的指定格式
    public static String formatTime(LocalDateTime time,String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    //获取当前时间的指定格式
    public static String formatNow(String pattern) {
        return  formatTime(LocalDateTime.now(), pattern);
    }

    //日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    //日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field){
        return time.minus(number,field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     * @param startTime
     * @param endTime
     * @param field  单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) return period.getYears();
        if (field == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return field.between(startTime, endTime);
    }

    //获取一天的开始时间，2017,7,22 00:00
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    //获取一天的结束时间，2017,7,22 23:59:59.999999999
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 根据2个时间返回 区间的年份
     * @param s
     * @param e
     * @return
     */
    public static List<Integer> getYearsBetweenTwoVar(LocalDate s, LocalDate e) {
        List<Integer> yearList = new ArrayList<>();
        while (s.isBefore(e)) {
            yearList.add(s.getYear());
            s = s.plusYears(1);
        }
        return yearList;
    }

    /**
     * 根据2个时间返回 区间的月份
     * @param s
     * @param e
     * @return
     */
    public static List<String> getMonthBetweenTwoVar(String s, String e) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate s1 = LocalDate.parse(s, dateTimeFormatter);
        LocalDate e1 = LocalDate.parse(e, dateTimeFormatter);
        if (e1.isBefore(s1)){
            return new ArrayList<>();
        }
        List<String> monthList = new ArrayList<>();
        while (true) {
            String localDate = s1.getYear() + "/" + (s1.getMonthValue()>9?s1.getMonthValue():"0" +s1.getMonthValue()) ;
            monthList.add(localDate);
            if ((s1.getYear() + "" + s1.getMonthValue()).equals(e1.getYear() + "" + e1.getMonthValue())){
                break;
            }
            s1 = s1.plusMonths(1);
        }
        return monthList;
    }


    /**
     * 根据2个时间返回 区间的日
     * @param s1
     * @param e1
     * @return
     */
    public static List<String> getDayBetweenTwoVar(LocalDate s1, LocalDate e1) {
        if (e1.isBefore(s1)){
            return new ArrayList<>();
        }
        List<String> dayList = new ArrayList<>();
        while (true) {
            String localDate = s1.getYear() + "-" + (s1.getMonthValue()>9?s1.getMonthValue():"0" +s1.getMonthValue()) + "-" + (s1.getDayOfMonth()>9?s1.getDayOfMonth():"0" +s1.getDayOfMonth());
            dayList.add(localDate);
            if ((s1.getYear() + "-" + s1.getMonthValue() + "-" +s1.getDayOfMonth() ).equals(e1.getYear() + "-" + e1.getMonthValue() + "-" + e1.getDayOfMonth())){
                break;
            }
            s1 = s1.plusDays(1);
        }
        return dayList;
    }


    public static void main(String[] args) {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate s1 = LocalDate.parse("2021-05-11", dateTimeFormatter);
//        LocalDate e1 = LocalDate.parse("2021-07-10", dateTimeFormatter);
//        List<String> integerList = getDayBetweenTwoVar(LocalDate.now(), e1);
//        integerList.forEach(System.out::println);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDay = LocalDate.parse( "2020/05" + "/01", dateTimeFormatter);
        LocalDate endDay = startDay.with(TemporalAdjusters.lastDayOfMonth());
        List<String> integerList = getDayBetweenTwoVar(startDay, endDay);
        integerList.forEach(System.out::println);
    }
}
