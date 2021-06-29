package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGolden;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.mainpage.domain.service.BackendToGoldenService;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 代志豪
 * 2021/6/17 17:04
 */
@Component
@Slf4j
public class FanInfoInit implements CommandLineRunner {

    public static Map<String, Integer> GOLDEN_ID_MAP = new HashMap<>();

    public static Map<String, Double> POWER_MAP = new HashMap<>();

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoldenUtil goldenUtil;

    @Autowired
    private BackendToGoldenService backendToGoldenService;

    @Autowired
    private FanInfoService fanInfoService;

    @Override
    public void run(String... args){
        BackendToGoldenQuery query = new BackendToGoldenQuery();
        query.setNumber(1);
        List<BackendToGolden> list = backendToGoldenService.getGoldenIdByBackendIdOrNumber(query);
        for (int i = 1; i < 64; i++) {
            for (BackendToGolden e : list) {
                GOLDEN_ID_MAP.put(FanConstant.GOLDEN_ID + e.getBackendId() + "_" + i, e.getGoldenId());
            }
        }
        for (int i = 1; i < 64; i++) {
            POWER_MAP.put(FanConstant.MONTHLY_POWER + i, 0.0);
        }
        POWER_MAP.put(FanConstant.DAILY_POWER_ALL, 0.0);
        POWER_MAP.put(FanConstant.MONTHLY_POWER_ALL, 0.0);
        POWER_MAP.put(FanConstant.ANNUAL_POWER_ALL, 0.0);
        POWER_MAP.put(FanConstant.DAILY_ONLINE_ALL, 0.0);
        POWER_MAP.put(FanConstant.MONTHLY_ONLINE_ALL, 0.0);
        POWER_MAP.put(FanConstant.ANNUAL_ONLINE_ALL, 0.0);
        try {
            fanInfoService.getStatistics("statistics");
            fanInfoService.getRuntimeInfos("runtime");
        }catch (Exception e){
            goldenUtil.cancelAll();
        }
        log.info("初始化数据完成！");
    }

//    /**
//     * 每日0:00执行
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void init() throws Exception {
//        int[] ids = {1, 2};
//        List<ValueData> power = goldenUtil.getSnapshots(ids);
//        for (int i = 1; i < power.size(); i++) {
//            redisUtil.set(FanConstant.DAILY_POWER + i, power.get(i).getValue());
//        }
//        double powerSum = power.stream().parallel().mapToDouble(ValueData::getValue).sum();
//        redisUtil.set(FanConstant.DAILY_POWER_ALL, powerSum);
//        List<ValueData> online = goldenUtil.getSnapshots(ids);
//        for (int i = 1; i < online.size(); i++) {
//            redisUtil.set(FanConstant.DAILY_ONLINE + i, online.get(i).getValue());
//        }
//        double onlineSum = online.stream().parallel().mapToDouble(ValueData::getValue).sum();
//        redisUtil.set(FanConstant.DAILY_POWER_ALL, onlineSum);
//    }
//
//    public double dataResetFloat(String key, Integer id, Integer type) {
//        try {
//            double value = goldenUtil.getFloat(id, getTime(type));
//            redisUtil.set(key, value);
//            return value;
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    public int dataResetInteger(String key, Integer id, Integer type) {
//        try {
//            int value = goldenUtil.getInteger(id, getTime(type));
//            redisUtil.set(key, value);
//            return value;
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//
//    /**
//     * 获取当日、月、年 0:00时间
//     *
//     * @param type 0：当日; 1:当月; 2:当年
//     * @return 时间字符串
//     */
//    private String getTime(Integer type) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(cal.get(Calendar.YEAR), type > 0 ? cal.get(Calendar.MONTH) : Calendar.JANUARY,
//                type == 1 ? cal.get(Calendar.DAY_OF_MONTH) : 1, 0, 0, 0);
//        Date beginOfDate = cal.getTime();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return formatter.format(beginOfDate);
//    }
//
//
//    public void goldenIdToRedis() {
//        BackendToGoldenQuery query = new BackendToGoldenQuery();
//        List<BackendToGolden> list = backendToGoldenService.getGoldenIdByBackendIdOrNumber(query);
//        List<Integer> collect = list.stream().parallel().distinct().map(BackendToGolden::getNumber).collect(Collectors.toList());
//        for (Integer i : collect) {
//            for (BackendToGolden e : list) {
//                redisUtil.setString(FanConstant.GOLDEN_ID + e.getBackendId() + "_" + i, e.getGoldenId().toString());
//            }
//        }
//    }
}
