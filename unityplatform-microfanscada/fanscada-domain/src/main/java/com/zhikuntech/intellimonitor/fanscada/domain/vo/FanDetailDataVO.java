package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: DAI
 * @date: Created in 2021/6/16 9:07
 * @description：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FanDetailDataVO {

    /**
     * ups数据
     */
    @ApiModelProperty("ups数据")
    @GoldenId(required = true)
    private UpsTelemetryVO upsData;

    /**
     * 直流屏数据
     */
    @ApiModelProperty("直流屏数据")
    @GoldenId(required = true)
    private DcScreenVO dcData;

    /**
     * ups状态
     */
    @ApiModelProperty("ups状态 0=关,1=开")
    @GoldenId(required = true)
    private UpsTelemetryStatusVO upsStatus;

    /**
     * 直流屏状态
     */
    @ApiModelProperty("直流屏状态 0=关,1=开")
    @GoldenId(required = true)
    private DcScreenStatusVO dcStatus;

}
