package com.zhikuntech.intellimonitor.fanscada.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailOneService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanDetailDataVO;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanModelDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: intelligence-monitor
 * @Description:
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@RestController
@RequestMapping("fanDetailOne")
@Api(tags = "风机详情")
public class FanDetailOneController {
    @Autowired
    private FanDetailOneService fanDetailOneService;

    @ApiOperation(value = "分图一#右侧数据")
    @ApiImplicitParam(name = "number", value = "风机编号(44)", paramType = "String", required = true)
    @GetMapping("/modelData")
    public BaseResponse<FanModelDataVO> getData(@RequestParam String number) {
        return fanDetailOneService.getData(number);
    }
}