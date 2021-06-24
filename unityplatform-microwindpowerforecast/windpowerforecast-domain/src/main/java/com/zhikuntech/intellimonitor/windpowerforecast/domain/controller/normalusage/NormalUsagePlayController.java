package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller.normalusage;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.WeatherHighDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfCurveDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.DqDayElectricGenDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.dto.NwpListPatternDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfCurvePatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfListPatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.query.NwpCurvePatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.NwpListPatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataCfService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataDqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataNwpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 常用展示
 *
 * @author liukai
 */
@Api(tags = "常用展示")
@RestController
@RequestMapping("/normal-usage-play")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NormalUsagePlayController {

    private final IWfDataNwpService nwpService;

    private final IWfDataCfService cfService;

    private final IWfDataDqService dqService;

    @ApiOperation("查询实测气象高度, 预测气象高度")
    @GetMapping("/high")
    public BaseResponse<WeatherHighDTO> fetchHigh() {
        WeatherHighDTO weatherHighDTO = new WeatherHighDTO();
        weatherHighDTO.setActHigh(cfService.queryHigh());
        weatherHighDTO.setVirtualHigh(nwpService.queryHigh());
        return BaseResponse.success(weatherHighDTO);
    }


    //# 预测气象数据

    @ApiOperation("曲线展示-曲线模式查询")
    @PostMapping("/query-nwp-curve")
    public BaseResponse<List<NwpListPatternDTO>> nwpCurveQuery(@RequestBody NwpCurvePatternQuery query) {
        List<NwpListPatternDTO> results = nwpService.nwpCurveQuery(query);
        return BaseResponse.success(results);
    }


    @ApiOperation("曲线展示-列表模式查询")
    @PostMapping("/query-nwp-list")
    public BaseResponse<Pager<NwpListPatternDTO>> nwpListQuery(@RequestBody NwpListPatternQuery query) {
        Pager<NwpListPatternDTO> results = nwpService.nwpListQuery(query);
        return BaseResponse.success(results);
    }

    @ApiOperation("日发电量计算")
    @GetMapping("/query-nwp-day-electric-gens")
    public BaseResponse<List<DqDayElectricGenDTO>> dayElectricGen() {
        List<DqDayElectricGenDTO> results = dqService.dayElectricGen();
        return BaseResponse.success(results);
    }

    //# 预测气象数据


    //# 实测气象数据 - 风玫瑰图

    @ApiOperation("风玫瑰图-曲线模式查询")
    @PostMapping("/query-cf-curve")
    public BaseResponse<List<CfCurveDTO>> cfCurveQuery(@RequestBody CfCurvePatternQuery query) {
        List<CfCurveDTO> results = cfService.cfCurveQuery(query);
        return BaseResponse.success(results);
    }

    @ApiOperation("风玫瑰图-列表模式查询")
    @PostMapping("/query-cf-list")
    public BaseResponse<Pager<CfListDTO>> cfListQuery(@RequestBody CfListPatternQuery query) {
        Pager<CfListDTO> results = cfService.cfListQuery(query);
        return BaseResponse.success(results);
    }

    //# 实测气象数据 - 风玫瑰图

}
