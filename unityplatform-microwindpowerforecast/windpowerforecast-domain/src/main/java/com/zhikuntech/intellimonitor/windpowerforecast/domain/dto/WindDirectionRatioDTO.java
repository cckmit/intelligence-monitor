package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 风向占比
 *
 * @author liukai
 */
@Data
public class WindDirectionRatioDTO {

    /**
     * 方向
     */
    private String direct;

    /**
     * 占比
     */
    private BigDecimal ratio;


}
