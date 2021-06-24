package com.zhikuntech.intellimonitor.mainpage.domain.service;

import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/7 16:10
 */
@Service
public interface FanInfoService {

    /**
     * 获取风机运行实时数据
     *
     * @return 全风场风机实时数据
     */
    List<FanRuntimeDTO> getRuntimeInfos() throws Exception;

    /**
     * 通过websocket直接返回前端
     *
     * @param username 用户名,用于区分客户端
     */
    void getRuntimeInfos(String username) throws Exception;

    /**
     * 获取风场发电情况
     *
     * @return 风场发电情况
     */
    FanStatisticsDTO getStatistics() throws Exception;

    /**
     * 通过websocket直接返回前端
     *
     * @param username 用户名,用于区分客户端
     */
    void getStatistics(String username) throws Exception;

    /**
     * 导出风机列表
     *
     * @throws Exception
     */
    void export(HttpServletResponse response) throws Exception;

}
