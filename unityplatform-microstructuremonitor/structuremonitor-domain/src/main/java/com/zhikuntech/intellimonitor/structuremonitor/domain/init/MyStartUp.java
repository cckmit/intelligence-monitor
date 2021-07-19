package com.zhikuntech.intellimonitor.structuremonitor.domain.init;


import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.structuremonitor.domain.constant.InitDataConstant;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGolden;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.StructureToGoldenService;
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
@Order
public class MyStartUp implements CommandLineRunner {

    public static Map<String, Integer> initMap = new HashMap<>();

    @Resource
    private StructureToGoldenService structureToGoldenService;

    @Override
    public void run(String... args) throws Exception {
        //todo 初始数据获取
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
        List<StructureToGolden> result = structureToGoldenService.getInitData(list);

        int[] fanNumbers = result.stream().mapToInt(StructureToGolden::getFanNumber).toArray();
        for (int fanNumber : fanNumbers) {
            for (StructureToGolden structureToGolden : result) {
                if (structureToGolden.getFanNumber() == fanNumber) {
                    initMap.put(InitDataConstant.STRUCTURE_TO_GOLDEN + structureToGolden.getFanNumber() + "_" + structureToGolden.getBackenId(), structureToGolden.getGoldenId());
                }
            }
        }
    }
}