package com.zhikuntech.intellimonitor.onlinemonitor.domain.schedule;

import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 代志豪
 * 2021/6/17 17:04
 */
@Component
@Slf4j
public class OnlineMonitorInit implements CommandLineRunner {

    @Value("${golden.ip}")
    private String ip;

    @Value("${golden.port}")
    private Integer port;

    @Value("${golden.user}")
    private String user;

    @Value("${golden.password}")
    private String password;

    @Value("${golden.poolSize}")
    private Integer poolSize;

    @Value("${golden.maxSize}")
    private Integer maxSize;

    public static Map<String, Integer> GOLDEN_ID_MAP = new HashMap<>();


    @Override
    public void run(String... args) {
        int[] goldenIds = {30404, 30405, 30406, 30407, 30408, 30409, 30410, 30411, 30412, 30413, 30414, 30415, 30416, 30417, 30418, 30419, 30420};
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 3; j++) {
                GOLDEN_ID_MAP.put(FanConstant.GOLDEN_ID + i + "_" + j, goldenIds[i - 1]);
            }
        }
        int[] idsGis = {135, 136};
        int[] goldenGis = {30404, 30405};
        for (Integer i : idsGis) {
            for (int j = 1; j < 41; j++) {
                GOLDEN_ID_MAP.put(FanConstant.GOLDEN_ID + i + "_" + j, goldenGis[i-135]);
            }
        }
        GoldenUtil.init(ip, port, user, password, poolSize, maxSize);
        log.info("初始化数据完成！");
    }

    /**
     * 自定义id与golden id转化
     *
     * @param ids 自定义id
     * @return golden ids
     */
    public static int[] getInts(int[] ids) {
        Set<Integer> idSet = new HashSet<>();
        for (Integer id : ids) {
            idSet.add(GOLDEN_ID_MAP.get(FanConstant.GOLDEN_ID + id + "_1"));
        }
        ids = idSet.stream().mapToInt(Integer::valueOf).toArray();
        return ids;
    }


//    /**
//     * 每日0:00执行
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void init() throws Exception {
//        int[] ids = {1, 2};
//        List<ValueData> power = GoldenUtil.getSnapshots(ids);
//        for (int i = 1; i < power.size(); i++) {
//            redisUtil.set(FanConstant.DAILY_POWER + i, power.get(i).getValue());
//        }
//        double powerSum = power.stream().parallel().mapToDouble(ValueData::getValue).sum();
//        redisUtil.set(FanConstant.DAILY_POWER_ALL, powerSum);
//        List<ValueData> online = GoldenUtil.getSnapshots(ids);
//        for (int i = 1; i < online.size(); i++) {
//            redisUtil.set(FanConstant.DAILY_ONLINE + i, online.get(i).getValue());
//        }
//        double onlineSum = online.stream().parallel().mapToDouble(ValueData::getValue).sum();
//        redisUtil.set(FanConstant.DAILY_POWER_ALL, onlineSum);
//    }
//
//    public double dataResetFloat(String key, Integer id, Integer type) {
//        try {
//            double value = GoldenUtil.getFloat(id, getTime(type));
//            redisUtil.set(key, value);
//            return value;
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    public int dataResetInteger(String key, Integer id, Integer type) {
//        try {
//            int value = GoldenUtil.getInteger(id, getTime(type));
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
