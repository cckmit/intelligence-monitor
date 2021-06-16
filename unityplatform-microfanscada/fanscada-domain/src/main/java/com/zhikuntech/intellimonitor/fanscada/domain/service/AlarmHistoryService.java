package com.zhikuntech.intellimonitor.fanscada.domain.service;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;

import java.util.List;

public interface AlarmHistoryService {
    BaseResponse<List<Object>> getList(String date, String code, String description, String level);
}
