package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: DAI
 * @date: Created in 2021/6/16 9:07
 * @description：
 */
@Data
public class FanDetailDataVO {

    /**
     * ups数据
     */
    @ApiModelProperty("ups数据")
    private UpsTelemetryVO upsData;

    /**
     * 直流屏数据
     */
    @ApiModelProperty("直流屏数据")
    private DcScreenVO dcData;

    /**
     * ups状态
     */
    @ApiModelProperty("ups状态 0=关,1=开")
    private UpsTelemetryStatusVO upsStatus;

    /**
     * 直流屏状态
     */
    @ApiModelProperty("直流屏状态 0=关,1=开")
    private DcScreenStatusVO dcStatus;

}
