package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assesscalc;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessChange;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessDay;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考核结果
 *
 * @author liukai
 * @date 2021/06/22
 */
public interface AssessCalcService {


    /**
     * 计算昨日漏报次数(短期/超短期)
     * <p>
     *     此处不需要关注预测电量的更改操作,
     *     因为更改操作需要在数据的次日凌晨2点之后才可以进行修改.
     *     比如 2021-05-01的日评估数据,
     *     需要在 2021-05-02的凌晨2点才可以进行
     * </p>
     *
     * @param bg 时间格式[yyyy-MM-dd]
     */
    void calcYesterdayAssess(String bg);


    /**
     * <p>
     *     修改日考核数据后, 如果是当月的数据则需要重新计算
     * </p>
     *
     * 上个月的日期
     * @param bg 上月日期
     */
    void calcDayAndMonthAssessElectric(String bg);


    /**
     * 计算月考核数据
     *
     * @param monthBg       月开始第一天
     * @param wfAssessDays  日考核数据
     * @param changeList    修改后的日考核数据
     */
    void calcMonthCheckData(LocalDateTime monthBg, List<WfAssessDay> wfAssessDays, List<WfAssessChange> changeList);

}
