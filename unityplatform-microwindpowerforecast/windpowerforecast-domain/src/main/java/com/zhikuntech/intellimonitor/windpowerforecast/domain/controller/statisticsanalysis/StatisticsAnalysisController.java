package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller.statisticsanalysis;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



    @ApiOperation("短期功率")
    @PostMapping("/query-dq")
    public void dq() {
        // TODO

    }

    @ApiOperation("超短期功率")
    @PostMapping("/query-cdq")
    public void cdq() {
        // TODO

    }

}
