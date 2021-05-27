package com.zhikuntech.intellimonitor.windpowerforecast.domain.controller;

import com.zhikuntech.intellimonitor.demo.facade.DemoFacade;
import com.zhikuntech.intellimonitor.demo.prototype.model.DemoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liukai
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private DemoFacade demoFacade;

    @GetMapping("/facade")
    public DemoModel demo() {
        return demoFacade.obtainColor();
    }

}
