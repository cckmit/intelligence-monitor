package com.zhikuntech.intellimonitor.onlinemonitor.domain.service;

import org.springframework.stereotype.Service;

/**
 * @author 代志豪
 * 2021/7/9 11:17
 */
@Service
public interface TransformerService {

    /**
     * 获取变压器实时数据
     */
    void getTransformerRuntime(String user, Integer num,String websocketDescription) throws Exception;

    void getCache(String username, Integer num);
}
