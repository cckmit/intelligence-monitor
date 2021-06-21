package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author： DAI
 * @date： Created in 2021/6/16 16:10
 * @description： ups状态
 */
@Data
public class UpsTelemetryStatusVO {

    @GoldenId(value = 103)
    @ApiModelProperty("UPS通讯故障")
    private Integer communicationFailure;

    @GoldenId(value = 104)
    @ApiModelProperty("UPS旁路异常")
    private Integer bypassAbnormal;

    @GoldenId(value = 105)
    @ApiModelProperty("UPS旁路供电")
    private Integer bypassSupply;

    @GoldenId(value = 106)
    @ApiModelProperty("UPS过载")
    private Integer upsOverload;

    @GoldenId(value = 107)
    @ApiModelProperty("UPS关机状态")
    private Integer shutdownStatus;

    @GoldenId(value = 108)
    @ApiModelProperty("UPS主输入异常")
    private Integer mainInputException;

    @GoldenId(value = 109)
    @ApiModelProperty("UPS电池欠压")
    private Integer batteryUnderVoltage;

    @GoldenId(value = 110)
    @ApiModelProperty("UPS故障")
    private Integer upsFault;

    @GoldenId(value = 111)
    @ApiModelProperty("UPS类型为后备式")
    private Integer backupType;

    @GoldenId(value = 132)
    @ApiModelProperty("24V电源1报警")
    private Integer powerAlarm1;

    @GoldenId(value = 133)
    @ApiModelProperty("24V电源2报警")
    private Integer powerAlarm2;

    @GoldenId(value = 172)
    @ApiModelProperty("网络状态1")
    private Integer networkStatus1;

    @GoldenId(value = 173)
    @ApiModelProperty("网络状态2")
    private Integer networkStatus2;

}
