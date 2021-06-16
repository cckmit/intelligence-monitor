package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: DAI
 * @date: Created in 2021/6/16 9:07
 * @description：
 */
@Data
public class FanLeftDataVO {

    /**
     * ups
     */
    @ApiModelProperty("ups")
    private UpsTelemetryVO ups;

    /**
     * 直流
     */
    @ApiModelProperty("直流")
    private DcScreenVO dc;

}
