package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess;

import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.ChangeResultDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.DayAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessDay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.AssessChangeQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.DayAssessQuery;

/**
 * <p>
 * 日考核结果 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
public interface IWfAssessDayService extends IService<WfAssessDay> {


    /**
     * 日考核结果-列表模式
     *
     * @param query 根据月份查询
     * @return  分页结果
     */
    Pager<DayAssessListDTO> queryDayList(DayAssessQuery query);


    /**
     * <p>
     *     1.当日不可修改
     *     2.当日之前[90天(可配置)]不可修改
     * </p>
     *
     * 日考核结果-修改数据
     * @param query 待修改的数据
     * @return      修改结果
     */
    ChangeResultDTO changeAssessData(AssessChangeQuery query);

}
