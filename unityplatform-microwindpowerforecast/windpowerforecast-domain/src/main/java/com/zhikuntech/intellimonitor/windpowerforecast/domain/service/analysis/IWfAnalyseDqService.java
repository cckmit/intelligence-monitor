package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.analysis;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseDq;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;

import java.util.List;

/**
 * <p>
 * 短期功率分析 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
public interface IWfAnalyseDqService extends IService<WfAnalyseDq> {

    /**
     * 分页查询短期功率
     * @param query 查询条件
     * @return  功率结果
     */
    DqListAggregateDTO dqPowerAnalysis(PowerAnalysisQuery query);
    //短期功率分析 曲线
    List<DqPowerAnalysisDTO> dqPowerAnalysisCurve(PowerAnalysisQuery query);

}
