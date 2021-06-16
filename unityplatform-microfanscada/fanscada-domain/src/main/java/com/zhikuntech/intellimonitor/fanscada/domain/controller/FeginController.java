package com.zhikuntech.intellimonitor.fanscada.domain.controller;

import com.zhikuntech.intellimonitor.fanscada.domain.service.FeginService;
import com.zhikuntech.intellimonitor.fanscada.prototype.dto.ActPowerDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author 滕楠
 * @className Outer
 * @create 2021/6/16 10:16
 **/
@RestController
@RequestMapping("/fegin")
public class FeginController {

    @Autowired
    FeginService feginService;

    @GetMapping("/getPowSum")
    public ActPowerDataDTO getActPower(){
        return feginService.getPowerSum();
    }
}