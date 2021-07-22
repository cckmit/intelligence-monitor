package com.zhikuntech.intellimonitor.structuremonitor.domain.init;


import com.zhikuntech.intellimonitor.structuremonitor.domain.constant.DataConstant;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMin;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.StructureToGoldenService;
import lombok.extern.slf4j.Slf4j;
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
 * @className MystartUp
 * @create 2021/7/9 14:28
 **/
@Component
@Order(2)
@Slf4j
public class MyStartUp implements CommandLineRunner {

    public static Map<String, Integer> initMap = new HashMap<>();

    @Resource
    private StructureToGoldenService structureToGoldenService;

    @Override
    public void run(String... args) throws Exception {
        // 初始映射关系数据获取
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(11);
        list.add(12);
        list.add(13);
        list.add(14);
        list.add(15);
        list.add(16);
        List<StructureToGoldenMin> result = structureToGoldenService.getInitData(list);
        if (result == null) {
            return;
        }
        int[] fanNumbers = result.stream().mapToInt(StructureToGoldenMin::getFanNumber).toArray();
        for (int fanNumber : fanNumbers) {
            for (StructureToGoldenMin structureToGoldenMin : result) {
                if (structureToGoldenMin.getFanNumber() == fanNumber) {
                    initMap.put(DataConstant.STRUCTURE_TO_GOLDEN + structureToGoldenMin.getFanNumber()+ "_" + structureToGoldenMin.getBackenId(), structureToGoldenMin.getGoldenId());
                }
            }
        }
    }
}