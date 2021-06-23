package com.zhikuntech.intellimonitor.fanscada.domain.config;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 滕楠
 * @className DataInitConf
 * @create 2021/6/21 15:40
 **/
@Component
@Slf4j
@Order(value = 2)
public class StartUpInitForPow implements CommandLineRunner {

    public static Map<String, Double> initMap = new HashMap<>();

    @Resource
    private BackendToGoldenService backendToGoldenService;

    @Resource
    private GoldenUtil goldenUtil;

    /**
     * 获取项目启动时零点的发电量数据
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(192);
        List<BackendToGolden> backendToGoldens = backendToGoldenService.selectList(list);
        int[] ints = backendToGoldens.stream().mapToInt(BackendToGolden::getGoldenId).toArray();
        List<ValueData> snapshots = goldenUtil.getSnapshots(ints);

        for (BackendToGolden backendToGolden : backendToGoldens) {
            for (ValueData snapshot : snapshots) {
                if (backendToGolden.getGoldenId() == snapshot.getId()) {
                    StartUpInitForPow.initMap.put(FanConstant.DAILY_POWER + backendToGolden.getNumber(), snapshot.getValue());
                }
            }
        }
        log.info("发电量初始化完成");
    }
}