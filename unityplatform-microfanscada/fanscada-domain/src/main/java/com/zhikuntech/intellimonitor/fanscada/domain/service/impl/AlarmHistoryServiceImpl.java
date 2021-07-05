package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.AlarmHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 滕楠
 * @className AlarmHistoryserviceImpl
 * @create 2021/6/15 17:36
 **/
@Service
public class AlarmHistoryServiceImpl implements AlarmHistoryService {

    @Autowired
    GoldenUtil goldenUtil;

    @Override
    public BaseResponse<List<Object>> getList(String date, String code, String description, String level) {

        return null;
    }
}