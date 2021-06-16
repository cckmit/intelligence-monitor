package com.zhikuntech.intellimonitor.fanscada.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailTwoService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanLeftDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: DAI
 * @date: Created in 2021/6/15 17:16
 * @description： 风机详情-分图二
 */
@RestController
@RequestMapping("fanDetailTwo")
@Api(tags = "风机详情")
public class FanDetailTwoController {

    @Autowired
    private FanDetailTwoService fanDetailTwoService;

    @ApiOperation(value = "分图二#左侧数据")
    @ApiImplicitParam(name = "fanId", value = "风机Id", paramType = "Integer", required = true)
    @GetMapping("/leftData/{fanId}")
    public BaseResponse<FanLeftDataVO> getLeftData(@PathVariable("fanId") Integer fanId) {
        return fanDetailTwoService.getLeftData(fanId);
    }
}
