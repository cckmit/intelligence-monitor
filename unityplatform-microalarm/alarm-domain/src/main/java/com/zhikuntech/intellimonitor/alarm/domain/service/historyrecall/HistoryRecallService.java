package com.zhikuntech.intellimonitor.alarm.domain.service.historyrecall;

import com.zhikuntech.intellimonitor.alarm.domain.dto.historyrecall.HistoryRecallDTO;
import com.zhikuntech.intellimonitor.alarm.domain.query.historyrecall.HistoryRecallQuery;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/7/19 16:20
 * @Description 事故追忆
 * @Version 1.0
 */
public interface HistoryRecallService {
    /**
     * 获取事故追忆数据
     * @param historyRecallQuery
     * @return
     */
    List<HistoryRecallDTO> getHistory(HistoryRecallQuery historyRecallQuery);
}
