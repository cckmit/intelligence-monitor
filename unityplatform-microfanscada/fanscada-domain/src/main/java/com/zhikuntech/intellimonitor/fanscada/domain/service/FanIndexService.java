package com.zhikuntech.intellimonitor.fanscada.domain.service;


import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.Loop;

import java.util.List;

public interface FanIndexService {


    /**
     * 查询回路
     * @return
     */
    BaseResponse<List<Loop>> getFanBaseInfoList();

}
