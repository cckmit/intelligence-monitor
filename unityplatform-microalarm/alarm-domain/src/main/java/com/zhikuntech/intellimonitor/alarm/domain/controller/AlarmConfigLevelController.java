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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation("删除数据-单条")
    @PostMapping("/delete/{level_no}")
    public BaseResponse<AlarmLevelDTO> deleteById(@PathVariable("level_no") String levelNo) {
        AlarmLevelDTO result = levelService.deleteByLevelNo(levelNo);
        return BaseResponse.success(result);
    }

    @ApiOperation("批量删除")
    @PostMapping("/delete-batch")
    public BaseResponse<List<AlarmLevelDTO>> batchDelete(@RequestBody List<String> levelNos) {
        List<AlarmLevelDTO> results = levelService.batchDelete(levelNos);
        return BaseResponse.success(results);
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public BaseResponse<AlarmLevelDTO> updateById(@RequestBody AlarmLevelDTO dto) {
        AlarmLevelDTO result = levelService.updateById(dto);
        return BaseResponse.success(result);
    }

    // TODO 导入 导出\


}
