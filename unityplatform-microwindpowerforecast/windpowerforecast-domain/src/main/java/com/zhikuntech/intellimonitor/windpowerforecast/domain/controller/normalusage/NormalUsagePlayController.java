package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller.normalusage;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.WeatherHighDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfListPatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataCfService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataNwpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private final IWfDataNwpService iWfDataNwpService;

    private final IWfDataCfService iWfDataCfService;

    @ApiOperation("查询实测气象高度, 预测气象高度")
    @GetMapping("/high")
    public BaseResponse<WeatherHighDTO> fetchHigh() {
        WeatherHighDTO weatherHighDTO = new WeatherHighDTO();
        weatherHighDTO.setActHigh(iWfDataCfService.queryHigh());
        weatherHighDTO.setVirtualHigh(iWfDataNwpService.queryHigh());
        return BaseResponse.success(weatherHighDTO);
    }

    /*
        TODO 曲线展示(列表模式) -> 待确认: 以哪个为基准(短期预测功率/超短期预测功率) [时间为15min]
            查询参数:
                时间
                数字天气预报
                实测气象

        TODO 曲线展示（曲线模式）-> 数据是否一致 (已确认, 数据一致)




        TODO 日发电量计算

     */


    //# 实测气象数据 - 风玫瑰图

    /*
        TODO 风向玫瑰图
        TODO 风能玫瑰图

        待确定问题,正好落在 N/NNE/NE等上面该如何计算
        待确定夹角问题


        ====>使用前闭合后开的统计方式

        16个方位:
            N
            NNE
            NE
            ENE
            E
            ESE
            SE
            SSE
            S
            SSW
            SW
            WSW
            W
            WNW
            NW
            NNW

     */


    @ApiOperation("风玫瑰图-列表模式查询")
    @PostMapping("/query-cf-list")
    public BaseResponse<Pager<CfListDTO>> cfListQuery(@RequestBody CfListPatternQuery query) {
        Pager<CfListDTO> results = iWfDataCfService.cfListQuery(query);
        return BaseResponse.success(results);
    }



    //# 实测气象数据 - 风玫瑰图

}
