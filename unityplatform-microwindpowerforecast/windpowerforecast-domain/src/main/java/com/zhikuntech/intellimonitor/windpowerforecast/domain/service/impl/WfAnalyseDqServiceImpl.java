package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseDq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAnalyseDqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAnalyseDqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 短期功率分析 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@Service
public class WfAnalyseDqServiceImpl extends ServiceImpl<WfAnalyseDqMapper, WfAnalyseDq> implements IWfAnalyseDqService {

    @Override
    public DqListAggregateDTO dqPowerAnalysis(PowerAnalysisQuery query) {
//        Pager<List<DqPowerAnalysisDTO>> results = new Pager<>();

        return null;
    }
}
