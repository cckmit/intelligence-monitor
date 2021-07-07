package com.zhikuntech.intellimonitor.demo.domain.controller;

import com.zhikuntech.intellimonitor.core.commons.utils.SFTPUtil;
import com.zhikuntech.intellimonitor.demo.prototype.model.DemoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
                .serverName("demo-app")
                .uid(UUID.randomUUID().toString())
                .color(UUID.randomUUID().toString())
                .build();
        log.info("ret demoModel: [{}]", demoModel);
        return demoModel;
    }

    @GetMapping("/header")
    public Map getHeader(HttpServletRequest request) {
        //获取所有请求头名称
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,String> map = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            //根据名称获取请求头的值
            String value = request.getHeader(name);
            map.put(name,value);
            log.info(name + "---" + value);
        }
        return map;
    }

}
