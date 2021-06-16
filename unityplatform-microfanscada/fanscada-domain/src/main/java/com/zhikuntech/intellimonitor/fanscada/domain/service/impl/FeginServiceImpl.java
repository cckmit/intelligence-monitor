package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;


import com.zhikuntech.intellimonitor.fanscada.domain.service.FeginService;
import com.zhikuntech.intellimonitor.fanscada.prototype.dto.ActPowerDataDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author 滕楠
 * @className OuterServiceImpl
 * @create 2021/6/16 10:19
 **/
@Service
public class FeginServiceImpl implements FeginService {


    @Override
    public ActPowerDataDTO getPowerSum() {

        //获取风机状态,获取有功功率,做和


        ActPowerDataDTO actPowerDataDTO = new ActPowerDataDTO();
        actPowerDataDTO.setActualProduce(BigDecimal.valueOf(0.0));
        return actPowerDataDTO;
    }
}