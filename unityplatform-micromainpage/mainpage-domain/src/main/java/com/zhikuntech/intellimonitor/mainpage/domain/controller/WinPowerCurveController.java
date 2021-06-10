package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.zhikuntech.intellimonitor.mainpage.domain.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 18:37
 * @Description 风功率曲线
 * @Version 1.0
 */
@Api(tags = "风功率曲线")
@RestController
@RequestMapping("/winPowerCurve")
public class WinPowerCurveController {
    @Autowired
    private WinPowerCurveService winPowerCurveService;

    @GetMapping("/getAll")
    @ApiOperation("获取所有时间内的【风功率曲线】数据")
    public BaseResponse<WindPowerCurveVO> getWindPowerCurveOfAllTime(){
        WindPowerCurveVO windPowerCurveVO = winPowerCurveService.getWindPowerCurveOfAllTime();
        return BaseResponse.success(windPowerCurveVO);
    }

    @GetMapping("/getCurrent")
    @ApiOperation("获取庚顿数据库中当前【风功率曲线】数据")
    public BaseResponse<WindPowerCurveVO> getWindPowerCurve() throws Exception {
        WindPowerCurveVO windPowerCurveVO = winPowerCurveService.getWindPowerCurve();
        return BaseResponse.success(windPowerCurveVO);
    }

    @GetMapping("/subscribe/{username}")
    @ApiOperation("订阅【风功率曲线】相关标签点快照改变的通知")
    @ApiImplicitParam(name = "username",value = "登录用户名")
    public BaseResponse<Boolean> subscribeWindPowerCurve(@PathVariable("username") String username) throws Exception {
        boolean result = winPowerCurveService.subscribeWindPowerCurve(username);
        return BaseResponse.success(result);
    }
}
