package com.zhikuntech.intellimonitor.integratedautomation.domain.service.impl;

import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.stream.DataConvertUtils;
import com.zhikuntech.intellimonitor.integratedautomation.domain.service.SeaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 代志豪
 * 2021/7/21 14:33
 */
@Slf4j
@Service
public class ControlCenterServiceImpl implements SeaService {
    @Override
    public void subscribe(String username, int[] ids) throws Exception {
        GoldenUtil.subscribeSnapshots("username", ids, (data) -> {
            long s = System.currentTimeMillis();
            DataConvertUtils.convertAndSend(data);
            long e = System.currentTimeMillis();
            log.info("数据{}条，推送kafka用时{},当前数据时间{}", data.length, e - s, data[0].getDate());
        });
    }
}
