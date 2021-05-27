package com.zhikuntech.intellimonitor.demo.facade;

import com.zhikuntech.intellimonitor.demo.prototype.model.DemoModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liukai
 */
@FeignClient(name = "demo-app", path = "/demo")
public interface DemoFacade {

    @GetMapping("/color")
    DemoModel obtainColor();
}
