package com.zhikuntech.intellimonitor.alarm.domain.controller;


import com.zhikuntech.intellimonitor.alarm.domain.dto.InnerAlarmRuleDTO;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AddNewAlarmRuleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmrule.AlarmRuleSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigRuleService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation("分页查询告警规则")
    @PostMapping("/query-by-page")
    public BaseResponse<Pager<InnerAlarmRuleDTO>> queryByPage(@RequestBody AlarmRuleSimpleQuery query) {
        Pager<InnerAlarmRuleDTO> results = ruleService.queryByPage(query);
        return BaseResponse.success(results);
    }

    /*
            告警策略(修改/删除)前提:
                如果存在告警信息关联相关规则, 是否可以修改对应的告警规则(直接修改, 无需关注)
     */

    @ApiOperation("修改告警规则")
    @PostMapping("/change-rule")
    public BaseResponse<InnerAlarmRuleDTO> changeRule(@RequestBody InnerAlarmRuleDTO query) {
        InnerAlarmRuleDTO result = ruleService.changeRule(query);
        return BaseResponse.success(result);
    }


    @ApiOperation("删除告警规则")
    @PostMapping("/delete/{rule_no}")
    public BaseResponse<Boolean> deleteRule(@PathVariable("rule_no") String ruleNo) {
        boolean result = ruleService.deleteRule(ruleNo);
        return BaseResponse.success(result);
    }

    @ApiOperation("批量删除告警规则")
    @PostMapping("/batch-delete")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<String> ruleNos) {
        boolean result = ruleService.batchDelete(ruleNos);
        return BaseResponse.success(result);
    }

}
