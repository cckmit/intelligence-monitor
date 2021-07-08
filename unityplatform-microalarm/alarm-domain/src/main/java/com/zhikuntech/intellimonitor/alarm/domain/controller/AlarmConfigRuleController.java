package com.zhikuntech.intellimonitor.alarm.domain.controller;


import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AddNewAlarmRuleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 规则表 前端控制器
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Api(tags = "告警策略")
@RestController
@RequestMapping("/alarm-config-rule")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigRuleController {


    private final IAlarmConfigRuleService ruleService;

    @ApiOperation("新增告警规则")
    @PostMapping("/add-new")
    public BaseResponse<Boolean> addNewAlarmRule(@RequestBody AddNewAlarmRuleQuery query) {
        boolean result = ruleService.addNewAlarmRule(query);
        return BaseResponse.success(result);
    }

    // TODO 分页查询告警规则



    /*
        TODO
            告警策略(修改/删除)前提:
                如果存在告警信息关联相关规则, 是否可以修改对应的告警规则
     */



}
