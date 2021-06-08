package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntime;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatistics;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 代志豪
 * 2021/6/8 10:27
 */
@Service
public class FanInfoServiceImpl implements FanInfoService {

    @Override
    public List<FanRuntime> getRuntimeInfos() {
        return null;
    }

    @Override
    public void getRuntimeInfos(String user) {

    }

    @Override
    public FanStatistics getStatistics() {
        return null;
    }

    @Override
    public void getStatistics(String user) {

    }
}
