package com.zhikuntech.intellimonitor.integratedautomation.domain.service;

import org.springframework.stereotype.Service;

/**
 * @author 代志豪
 * 2021/7/21 14:38
 */
@Service
public interface IntegratedAutomationService {
    void subscribe(String username, int[] ids);
}
