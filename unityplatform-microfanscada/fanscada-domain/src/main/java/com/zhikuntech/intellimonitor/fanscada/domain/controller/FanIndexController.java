package com.zhikuntech.intellimonitor.fanscada.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.Loop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 滕楠
 * @className FanIndexController
 * @create 2021/6/15 10:53
 **/
@RestController
@RequestMapping("fanIndex")
@Api(tags = "风机首页")
public class FanIndexController {

    @Autowired
    private FanIndexService fanIndexService;

    @ApiOperation("")
    @GetMapping()
    public BaseResponse<List<Loop>> getFanBaseInfoList() {
        return this.fanIndexService.getFanBaseInfoList();
    }


}