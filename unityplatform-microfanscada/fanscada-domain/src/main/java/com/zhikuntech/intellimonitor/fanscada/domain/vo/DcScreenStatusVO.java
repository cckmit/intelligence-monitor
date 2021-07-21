package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author： DAI
 * @date： Created in 2021/6/16 16:24
 * @description： 直流屏状态
 */
@Data
public class DcScreenStatusVO {

    @GoldenId(value = 40)
    @ApiModelProperty("直流屏通讯故障")
    private Integer communicationFailure;

    @GoldenId(value = 41)
    @ApiModelProperty("直流屏电池组欠压")
    private Integer batteryUnderVoltage;

    @GoldenId(value = 42)
    @ApiModelProperty("直流屏单体电池故障")
    private Integer singleBatteryFailure;

    @GoldenId(value = 43)
    @ApiModelProperty("直流屏母线欠压")
    private Integer busUnderVoltage;

    @GoldenId(value = 44)
    @ApiModelProperty("直流屏母线绝缘预警")
    private Integer busInsulationWarning;

    @GoldenId(value = 45)
    @ApiModelProperty("直流屏母线交流串入故障")
    private Integer busAcSeriesFault;

    @GoldenId(value = 46)
    @ApiModelProperty("直流屏Host交流缺相")
    private Integer hostPhaseLoss;

    @GoldenId(value = 47)
    @ApiModelProperty("直流屏#1JLCL交流故障")
    private Integer acFault1;

    @GoldenId(value = 48)
    @ApiModelProperty("直流屏#1JLCL交流失电")
    private Integer acPowerLoss1;

    @GoldenId(value = 49)
    @ApiModelProperty("直流屏#2JLCL交流缺相")
    private Integer acPhaseLoss2;

    @GoldenId(value = 50)
    @ApiModelProperty("直流屏电池组过压")
    private Integer overVoltageOfBattery;

    @GoldenId(value = 51)
    @ApiModelProperty("直流屏电池组过温")
    private Integer overVoltageOfTemperature;

    @GoldenId(value = 52)
    @ApiModelProperty("直流屏母线过压")
    private Integer busOvervoltage;

    @GoldenId(value = 53)
    @ApiModelProperty("直流屏母线绝缘异常")
    private Integer busInsulationAbnormal;

    @GoldenId(value = 54)
    @ApiModelProperty("直流屏母线绝缘压差故障")
    private Integer busInsulationFault;

    @GoldenId(value = 55)
    @ApiModelProperty("直流屏Host交流故障")
    private Integer hostAcFault;

    @GoldenId(value = 56)
    @ApiModelProperty("直流屏Host交流失电")
    private Integer hostPowerLoss;

    @GoldenId(value = 57)
    @ApiModelProperty("直流屏#2JLCL交流故障")
    private Integer acFault2;

    @GoldenId(value = 58)
    @ApiModelProperty("直流屏#2JLCL交流失电")
    private Integer acPowerLoss2;

    @GoldenId(value = 59)
    @ApiModelProperty("直流屏#1JLCL交流缺相")
    private Integer acPhaseLoss1;
}
