package com.zhikuntech.intellimonitor.integratedautomation.facade;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author 杨锦程
 * @Date 2021/7/22 14:21
 * @Description 告警配置
 * @Version 1.0
 */
@FeignClient(name = "integratedautomation-app")
public interface AlarmConfigMonitorFeign {
    /**
     * 根据页面标识查询庚顿数据库id
     * @param pageIdentifies
     * @return
     */
    @GetMapping("/alarmConfig/getGoldenIdByPageIdentifies")
    BaseResponse getGoldenIdByPageIdentifies(@RequestParam("pageIdentifies") String[] pageIdentifies);
}
