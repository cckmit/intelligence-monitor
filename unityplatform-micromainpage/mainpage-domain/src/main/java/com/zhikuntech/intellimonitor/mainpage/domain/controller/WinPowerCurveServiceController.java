package com.zhikuntech.intellimonitor.mainpage.domain.controller;

import com.zhikuntech.intellimonitor.mainpage.domain.base.BaseResponse;
import com.zhikuntech.intellimonitor.mainpage.domain.service.WinPowerCurveService;
import com.zhikuntech.intellimonitor.mainpage.domain.vo.WindPowerCurveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 杨锦程
 * @Date 2021/6/8 18:37
 * @Description ${description}
 * @Version 1.0
 */
@RestController
@RequestMapping("/winPowerCurve")
public class WinPowerCurveServiceController {
    @Autowired
    private WinPowerCurveService winPowerCurveService;

    @GetMapping("/getCurrent")
    public BaseResponse<WindPowerCurveVO> getWindPowerCurve() throws Exception {
        WindPowerCurveVO windPowerCurveVO = winPowerCurveService.getWindPowerCurve();
        return BaseResponse.success(windPowerCurveVO);
    }

    @GetMapping("/subscribe/{username}")
    public BaseResponse<Boolean> subscribeWindPowerCurve(@PathVariable("username") String username){
        boolean result = winPowerCurveService.subscribeWindPowerCurve(username);
        return BaseResponse.success(result);
    }
}
