package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.dqcalc;

/**
 * @author liukai
 */
public interface DqCalcService {


    /**
     * 短期数据计算
     *
     * @param bg            开始时间
     * @param end           结束时间
     * @param headerDate    头部时间
     */
    void daDataCalc(String bg, String end, String headerDate);

}
