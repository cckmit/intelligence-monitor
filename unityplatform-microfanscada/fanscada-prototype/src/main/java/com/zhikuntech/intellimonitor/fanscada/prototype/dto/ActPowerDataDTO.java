package com.zhikuntech.intellimonitor.fanscada.prototype.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 实时功率数据
 *
 * @author liukai
 */
@Data
public class ActPowerDataDTO {

    /**
     * 实时功率
     */
    private BigDecimal actualProduce;

}
