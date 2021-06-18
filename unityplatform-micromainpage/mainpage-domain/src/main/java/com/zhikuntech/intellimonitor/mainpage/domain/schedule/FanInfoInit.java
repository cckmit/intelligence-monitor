package com.zhikuntech.intellimonitor.mainpage.domain.schedule;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/17 17:04
 */
@Component
@Slf4j
public class FanInfoInit {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoldenUtil goldenUtil;

    /**
     * 每日0:00执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void init() throws Exception {
        int[] ids = {1, 2};
        List<ValueData> power = goldenUtil.getSnapshots(ids);
        for (int i = 1; i < power.size(); i++) {
            redisUtil.set(FanConstant.DAILY_POWER + i, power.get(i).getValue());
        }
        double powerSum = power.stream().parallel().mapToDouble(ValueData::getValue).sum();
        redisUtil.set(FanConstant.DAILY_POWER_ALL, powerSum);
        List<ValueData> online = goldenUtil.getSnapshots(ids);
        for (int i = 1; i < online.size(); i++) {
            redisUtil.set(FanConstant.DAILY_ONLINE + i, online.get(i).getValue());
        }
        double onlineSum = online.stream().parallel().mapToDouble(ValueData::getValue).sum();
        redisUtil.set(FanConstant.DAILY_POWER_ALL, onlineSum);
    }

    public double dataReset(String key, Integer id, Integer month, Integer day) {
        try {
            double value = goldenUtil.getFloat(id, getTime(month, day));
            redisUtil.set(key, value);
            return value;
        } catch (Exception e) {
            return 0;
        }
    }


    private static String getTime(Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), null == month ? cal.get(Calendar.MONTH) : month,
                null == day ? cal.get(Calendar.DAY_OF_MONTH) : day, 0, 0, 0);
        Date beginOfDate = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(beginOfDate);
    }
}
