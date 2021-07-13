package com.zhikuntech.intellimonitor.onlinemonitor.domain.service;

import org.springframework.stereotype.Service;

/**
 * @Author 杨锦程
 * @Date 2021/7/12 17:06
 * @Description GIS
 * @Version 1.0
 */
@Service
public interface StationGISService {
    /**
     * 获取GIS实时数据
     */
    void getGISRuntime(String user, Integer num) throws Exception;
}
