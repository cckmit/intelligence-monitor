package com.zhikuntech.intellimonitor.structuremonitor.domain.service;

import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenAvg;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMax;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMin;

import java.util.List;

/**
 * @author tn
 */
public interface StructureToGoldenService {
        /**
         * 获取最小值id之间的映射关系
         * @param fanNumber 风机编号
         * @param dateType 1:加速度 2:沉降
         * @return
         */
        List<StructureToGoldenMin> getMinMap(Integer fanNumber, Integer dateType);

        /**
         * 获取最大值id之间的映射关系
         * @param fanNumber
         * @param dateType
         * @return
         */
        List<StructureToGoldenMax> getMaxMap(Integer fanNumber, Integer dateType);

        /**
         * 获取平均值id之间的映射关系
         * @param fanNumber
         * @param dateType
         * @return
         */
        List<StructureToGoldenAvg> getAvgMap(Integer fanNumber, Integer dateType);

        /** :
         * @param list
         * @return java.util.List<com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGolden>
         * @author Tn
         * @date 2021/7/20 18:21
         */
        List<StructureToGoldenMin> getInitData(List<Integer> list);
}
