package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;


import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FeginService;
import com.zhikuntech.intellimonitor.fanscada.prototype.dto.ActPowerDataDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
        int[] a = {12};
        try {
            List<ValueData> snapshots = GoldenUtil.getSnapshots(a);
            ValueData valueData = snapshots.get(0);
            Double value = valueData.getValue();
            ActPowerDataDTO actPowerDataDTO = new ActPowerDataDTO();
            actPowerDataDTO.setActualProduce(BigDecimal.valueOf(value));
            return actPowerDataDTO;
        } catch (Exception e) {
            e.printStackTrace();
            ActPowerDataDTO actPowerDataDTO = new ActPowerDataDTO();
            actPowerDataDTO.setActualProduce(BigDecimal.valueOf(0.0));
            return actPowerDataDTO;
        }


    }
}
