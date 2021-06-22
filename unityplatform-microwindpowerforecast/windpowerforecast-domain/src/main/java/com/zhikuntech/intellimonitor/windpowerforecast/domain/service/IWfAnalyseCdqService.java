package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseCdq;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;

import java.util.List;

/**
 * <p>
 * 超短期功率分析 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
public interface IWfAnalyseCdqService extends IService<WfAnalyseCdq> {

    /**
     * 超短期功率分析
     * @param query 查询条件
     * @return 超短期分页查询结果
     */
    CdqListAggregateDTO cdqPowerAnalysis(PowerAnalysisQuery query);
    //超短期功率分析 曲线
    List<CdqPowerAnalysisDTO> cdqPowerAnalysisCurve(PowerAnalysisQuery query);
}
