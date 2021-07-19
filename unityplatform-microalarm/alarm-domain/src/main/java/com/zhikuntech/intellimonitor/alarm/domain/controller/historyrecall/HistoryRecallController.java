package com.zhikuntech.intellimonitor.alarm.domain.controller.historyrecall;

import com.zhikuntech.intellimonitor.alarm.domain.query.historyrecall.HistoryRecallQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.historyrecall.HistoryRecallService;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "事故追忆")
@RestController
@RequestMapping("/accident")
@Slf4j
public class HistoryRecallController {
    @Resource
    private HistoryRecallService historyRecallService;

    /**
     * 获取事故追忆数据
     * @param historyRecallQuery
     * @return
     */
    @ApiOperation("获取事故追忆数据")
    @PostMapping("/getHistory")
    public BaseResponse getArchivedValues(@Valid @RequestBody HistoryRecallQuery historyRecallQuery){
        log.info("获取事故追忆数据,参数historyRecallQuery->{}",historyRecallQuery);
        return BaseResponse.success(historyRecallService.getHistory(historyRecallQuery));
    }

}
