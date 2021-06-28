package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.cdqcalc;

/**
 * @author liukai
 */
public interface CdqCalcService {

    /**
     * 需要保证三个日期在同一天时间内
     * bg  ->  yyyy-MM-dd HH:mm:ss
     * end ->  yyyy-MM-dd HH:mm:ss
     * headerDate  ->  yyyy-MM-dd HH:mm:ss
     *
     * 计算短期分析数据/评估数据
     * @param bg            开始日期
     * @param end           结束日期
     * @param headerDate    头部日期
     */
    void calcData(String bg, String end, String headerDate);


}
