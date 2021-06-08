package com.zhikuntech.intellimonitor.mainpage.domain.service;

import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntime;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatistics;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 代志豪
 * 2021/6/7 16:10
 */
@Service
public interface FanInfoService {

    /**
     * 获取风机运行实时数据
     * @return 全风场风机实时数据
     */
    List<FanRuntime> getRuntimeInfos();

    /**
     * 通过websocket直接返回前端
     * @param user 用户名,用于区分客户端
     */
     void getRuntimeInfos(String user);

    /**
     * 获取风场发电情况
     * @return 风场发电情况
     */
    FanStatistics getStatistics();
}
