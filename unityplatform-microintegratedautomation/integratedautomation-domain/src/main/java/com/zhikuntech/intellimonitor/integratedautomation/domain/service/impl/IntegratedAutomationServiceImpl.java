package com.zhikuntech.intellimonitor.integratedautomation.domain.service.impl;

import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.TimerUtil;
import com.zhikuntech.intellimonitor.core.stream.DataConvertUtils;
import com.zhikuntech.intellimonitor.integratedautomation.domain.service.IntegratedAutomationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

/**
 * @author 代志豪
 * 2021/7/21 14:33
 */
@Slf4j
@Service
public class IntegratedAutomationServiceImpl implements IntegratedAutomationService {
    /**
     * 订阅庚顿
     *
     * @param goldenName 庚顿订阅连接名
     * @param ids        庚顿订阅标签点数组
     */
    @Override
    public void subscribe(String goldenName, int[] ids) {
        try {
            startTimer(goldenName, ids);
            GoldenUtil.subscribeSnapshots(goldenName, ids, (data) -> {
                long s = System.currentTimeMillis();
                startTimer(goldenName, ids);
                DataConvertUtils.convertAndSend(data);
                long e = System.currentTimeMillis();
                log.info("数据{}条，推送kafka用时{},当前数据时间{}", data.length, e - s, data[0].getDate());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时任务，golden连接断开或者无响应之后，重启订阅
     * @param user  庚顿订阅连接名
     * @param ids   庚顿订阅标签点数组
     */
    private void startTimer(String user, int[] ids) {
        TimerUtil.start(new TimerTask() {
            @Override
            public void run() {
                GoldenUtil.cancel(user);
                log.info("定时任务取消golden连接,{}", user);
                subscribe(user, ids);
            }
        }, user);
    }
}
