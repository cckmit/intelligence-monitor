package com.zhikuntech.intellimonitor.fanscada.facade.feign;


import com.zhikuntech.intellimonitor.fanscada.prototype.dto.ActPowerDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "fanscada-app")
public interface ToWindPowerForecast {

    @GetMapping("/fegin/getPowSum")
    ActPowerDataDTO getPowerSum();

}
