package com.zhikuntech.intellimonitor.demo.domain.controller;

import com.zhikuntech.intellimonitor.demo.domain.model.DemoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author liukai
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {


    @GetMapping("/color")
    public DemoModel fetchColor() {
        DemoModel demoModel = DemoModel.builder()
                .color(UUID.randomUUID().toString())
                .build();
        log.info("ret demoModel: [{}]", demoModel);
        return demoModel;
    }
}
