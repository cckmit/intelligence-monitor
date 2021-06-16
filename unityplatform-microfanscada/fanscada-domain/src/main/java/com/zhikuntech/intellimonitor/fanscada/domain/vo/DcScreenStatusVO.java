package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author： DAI
 * @date： Created in 2021/6/16 16:24
 * @description： 直流屏状态
 */
@Data
public class DcScreenStatusVO {

    @GoldenId(value = 112)
    @ApiModelProperty("直流屏通讯故障")
    private Integer communicationFailure;

    @GoldenId(value = 113)
    @ApiModelProperty("直流屏电池组欠压")
    private Integer batteryUnderVoltage;

    @GoldenId(value = 114)
    @ApiModelProperty("直流屏单体电池故障")
    private Integer singleBatteryFailure;

    @GoldenId(value = 115)
    @ApiModelProperty("直流屏母线欠压")
    private Integer busUnderVoltage;

    @GoldenId(value = 116)
    @ApiModelProperty("直流屏母线绝缘预警")
    private Integer busInsulationWarning;

    @GoldenId(value = 117)
    @ApiModelProperty("直流屏母线交流串入故障")
    private Integer busAcSeriesFault;

    @GoldenId(value = 118)
    @ApiModelProperty("直流屏Host交流缺相")
    private Integer hostPhaseLoss;

    @GoldenId(value = 119)
    @ApiModelProperty("直流屏#1JLCL交流故障")
    private Integer acFault1;

    @GoldenId(value = 120)
    @ApiModelProperty("直流屏#1JLCL交流失电")
    private Integer acPowerLoss1;

    @GoldenId(value = 121)
    @ApiModelProperty("直流屏#2JLCL交流缺相")
    private Integer acPhaseLoss2;

    @GoldenId(value = 122)
    @ApiModelProperty("直流屏电池组过压")
    private Integer overVoltageOfBattery;

    @GoldenId(value = 123)
    @ApiModelProperty("直流屏电池组过温")
    private Integer overVoltageOfTemperature;

    @GoldenId(value = 124)
    @ApiModelProperty("直流屏母线过压")
    private Integer busOvervoltage;

    @GoldenId(value = 125)
    @ApiModelProperty("直流屏母线绝缘异常")
    private Integer busInsulationAbnormal;

    @GoldenId(value = 126)
    @ApiModelProperty("直流屏母线绝缘压差故障")
    private Integer busInsulationFault;

    @GoldenId(value = 127)
    @ApiModelProperty("直流屏Host交流故障")
    private Integer hostAcFault;

    @GoldenId(value = 128)
    @ApiModelProperty("直流屏Host交流失电")
    private Integer hostPowerLoss;

    @GoldenId(value = 129)
    @ApiModelProperty("直流屏#2JLCL交流故障")
    private Integer acFault2;

    @GoldenId(value = 130)
    @ApiModelProperty("直流屏#2JLCL交流失电")
    private Integer acPowerLoss2;

    @GoldenId(value = 131)
    @ApiModelProperty("直流屏#1JLCL交流缺相")
    private Integer acPhaseLoss1;
}
