package com.zhikuntech.intellimonitor.integratedautomation.domain.service;

import org.springframework.stereotype.Service;

/**
 * @author 代志豪
 * 2021/7/20 14:08
 */
@Service
public interface ControlCenterService {
    void subscribe(String username, int[] ids) throws Exception;
}
