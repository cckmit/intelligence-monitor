package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller.statisticsanalysis;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.analysis.IWfAnalyseCdqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.analysis.IWfAnalyseDqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *     统计分析:
 *     1.短期功率分析
 *     2.超短期功率分析
 * </p>
 * @author liukai
 */
@Api(tags = "统计分析")
@RestController
@RequestMapping("/statistics-analysis")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticsAnalysisController {

    private final IWfAnalyseDqService dqService;

    private final IWfAnalyseCdqService cdqService;

    @ApiOperation("功率分析-短期功率")
    @PostMapping("/query-dq")
    public BaseResponse<DqListAggregateDTO> dqPowerAnalysis(@RequestBody PowerAnalysisQuery query) {
        DqListAggregateDTO result = dqService.dqPowerAnalysis(query);
        return BaseResponse.success(result);
    }

    @ApiOperation("功率分析-超短期功率")
    @PostMapping("/query-cdq")
    public BaseResponse<CdqListAggregateDTO> cdqPowerAnalysis(@RequestBody PowerAnalysisQuery query) {
        CdqListAggregateDTO result = cdqService.cdqPowerAnalysis(query);
        return BaseResponse.success(result);
    }


    @ApiOperation("功率分析-短期功率-曲线")
    @PostMapping("/query-dq-curve")
    public BaseResponse<List<DqPowerAnalysisDTO>> dqPowerAnalysisCurve(@RequestBody PowerAnalysisQuery query) {
       List<DqPowerAnalysisDTO> result = dqService.DqPowerAnalysisCurve(query);
       return BaseResponse.success(result);
    }

    @ApiOperation("功率分析-超短期功率-曲线")
    @PostMapping("/query-cdq-curve")
    public BaseResponse<List<CdqPowerAnalysisDTO>> cdqPowerAnalysisCurve(@RequestBody PowerAnalysisQuery query) {
        List<CdqPowerAnalysisDTO> result = cdqService.cdqPowerAnalysisCurve(query);
        return BaseResponse.success(result);
    }
}
