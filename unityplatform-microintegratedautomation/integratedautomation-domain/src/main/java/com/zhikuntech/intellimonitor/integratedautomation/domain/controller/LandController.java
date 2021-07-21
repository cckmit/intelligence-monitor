package com.zhikuntech.intellimonitor.integratedautomation.domain.controller;

import com.zhikuntech.intellimonitor.integratedautomation.domain.service.LandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 代志豪
 * 2021/7/20 15:18
 */
@RestController
@RequestMapping("/land")
@Api(tags = "陆上站")
public class LandController {

    @Autowired
    private LandService landService;

    @PostMapping("/subscribe")
    @ApiOperation("数据推送kafka")
    public void subscribe() throws Exception{
        landService.subscribe("",new int[]{1,3});
    }
}
