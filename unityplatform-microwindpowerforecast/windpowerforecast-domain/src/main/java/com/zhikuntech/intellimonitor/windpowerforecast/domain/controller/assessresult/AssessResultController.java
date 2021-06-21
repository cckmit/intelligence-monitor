package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller.assessresult;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthAssessQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("月考核结果-列表模式")
    @PostMapping("/month-list-pattern")
    public BaseResponse<Pager<MonthAssessListDTO>> queryMonthList(@RequestBody MonthAssessQuery query) {
        Pager<MonthAssessListDTO> pageResult = monthService.queryMonthList(query);
        return BaseResponse.success(pageResult);
    }


    // TODO 日考核结果 列表模式




    // TODO 曲线模式（日 + 月）




    // TODO 修改数据




}
