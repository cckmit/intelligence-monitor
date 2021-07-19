package com.zhikuntech.intellimonitor.structuremonitor.domain.service;

import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGolden;

import java.util.List;

public interface StructureToGoldenService {
        /**
         * 获取id之间的映射关系
         * @return
         */
        List<StructureToGolden> getMap(Integer fanNumber);

        List<StructureToGolden> getInitData(List<Integer> list);
}
