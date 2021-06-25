package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.windpowerforecast.facade.ForeCastCurveFacade;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.dto.NwpListPatternDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.prototype.query.NwpCurvePatternQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 18:37
 * @Description 风功率曲线
 * @Version 1.0
 */
@Api(tags = "WinPowerCurveController",description = "风功率曲线")
@RestController
@RequestMapping("/winPowerCurve")
@Slf4j
public class WinPowerCurveController {
    @Autowired
    private ForeCastCurveFacade foreCastCurveFacade;

    @PostMapping("/getAll")
    @ApiOperation("获取所有时间内的【风功率曲线】数据")
    public BaseResponse getWindPowerCurveOfAllTime(@RequestBody NwpCurvePatternQuery nwpCurvePatternQuery){
        BaseResponse<List<NwpListPatternDTO>> listBaseResponse = foreCastCurveFacade.nwpCurveQuery(nwpCurvePatternQuery);
        log.info("获取所有时间内的【风功率曲线】数据 nwpListPatternDTOS:{}{}","\n",listBaseResponse);
        return listBaseResponse;
    }
}
