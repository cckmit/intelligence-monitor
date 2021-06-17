package com.zhikuntech.intellimonitor.fanscada.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FsBasicParameterVO;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailTwoService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanDetailDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "分图二数据")
    @ApiImplicitParam(name = "number", value = "风机编号 目前可传任意值 ", paramType = "String", required = true)
    @GetMapping("/queryData")
    public BaseResponse<FanDetailDataVO> getData(@RequestParam String number) {
        return fanDetailTwoService.getData(number);
    }

    @ApiOperation(value = "分图二#风机参数")
    @ApiImplicitParam(name = "number", value = "风机编号 目前固定参数 #44 ", paramType = "String", required = true)
    @GetMapping("/queryParameter")
    public BaseResponse<FsBasicParameterVO> getFanParameter(@RequestParam String number) {
        return fanDetailTwoService.getFanParameterByNumber(number);
    }
}
