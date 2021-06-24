package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller.assessresult;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.ChangeResultDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.DayAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessCurveDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessDayService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考核结果
 *
 * @author liukai
 */
@Api(tags = "考核结果")
@RestController
@RequestMapping("/assess-result")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssessResultController {

    private final IWfAssessMonthService monthService;

    private final IWfAssessDayService dayService;

    @ApiOperation("月考核结果数据更新")
    @PostMapping("/month-update")
    public BaseResponse<ChangeResultDTO> monthAssessUpdate(@RequestBody MonthAssessUpdateQuery query) {
        ChangeResultDTO result = monthService.monthAssessUpdate(query);
        return BaseResponse.success(result);
    }

    @ApiOperation("月考核结果-列表模式")
    @PostMapping("/month-list-pattern")
    public BaseResponse<Pager<MonthAssessListDTO>> queryMonthList(@RequestBody MonthAssessQuery query) {
        Pager<MonthAssessListDTO> pageResult = monthService.queryMonthList(query);
        return BaseResponse.success(pageResult);
    }

    @ApiOperation("曲线模式（月）")
    @PostMapping("/month-curve-pattern")
    public BaseResponse<List<MonthAssessCurveDTO>> queryMonthCurve(@RequestBody MonthCurveQuery query) {
        List<MonthAssessCurveDTO> results = monthService.queryMonthCurve(query);
        return BaseResponse.success(results);
    }

    @ApiOperation("日考核结果-列表模式")
    @PostMapping("/day-list-pattern")
    public BaseResponse<Pager<DayAssessListDTO>> queryDayList(@RequestBody DayAssessQuery query) {
        Pager<DayAssessListDTO> pageResult = dayService.queryDayList(query);
        return BaseResponse.success(pageResult);
    }

    @ApiOperation("日考核结果-修改数据")
    @PostMapping("/change-assess-data")
    public BaseResponse<ChangeResultDTO> changeAssessData(@RequestBody AssessChangeQuery query) {
        ChangeResultDTO result = dayService.changeAssessData(query);
        return BaseResponse.success(result);
    }

}
