package com.zhikuntech.intellimonitor.alarm.domain.controller;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AddNewAlarmLevelQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AlarmLevelSimpleQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmConfigLevelService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
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
 * 告警等级表 前端控制器
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Api(tags = "告警等级配置")
@RestController
@RequestMapping("/alarm-config-level")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmConfigLevelController {


    private final IAlarmConfigLevelService levelService;

    /*
        1）新增：点击展示告警等级新增弹窗，新增字段包括告警等级名称、告警方式、备注

  a.告警策略名称：必填项，不可重复；

  b.告警方式：目前可选择项包括闪烁、闪烁+音响、闪烁+弹窗，可配置；

  c.备注：可添加备注内容；

  2）删除：支持单条、批量、全部删除；

  3）导出：支持导出为Excel；

  4）导入：支持按Excel模板导入告警等级；
     */

    // 列表字段：包括序号、告警等级、告警方式、备注


    @ApiOperation("新增告警等级")
    @PostMapping("/add-new")
    public BaseResponse<Boolean> addNewAlarmLevel(@RequestBody AddNewAlarmLevelQuery query) {
        boolean result = levelService.addNewAlarmLevel(query);
        return BaseResponse.success(result);
    }

    @ApiOperation("查询告警等级-分页")
    @PostMapping("/query-levels")
    public BaseResponse<Pager<AlarmLevelDTO>> queryAlarmLevel(@RequestBody AlarmLevelSimpleQuery query) {
        Pager<AlarmLevelDTO> results = levelService.queryAlarmLevel(query);
        return BaseResponse.success(results);
    }

    // todo 删除


}
