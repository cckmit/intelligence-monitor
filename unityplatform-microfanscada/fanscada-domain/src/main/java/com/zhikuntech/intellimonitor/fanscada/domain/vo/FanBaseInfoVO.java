package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 滕楠
 * @className FanBaseInfo
 * @create 2021/6/11 17:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "",description = "首页风机返回类")
public class FanBaseInfoVO {

    @ApiModelProperty("编号")
    private Integer fanNumber;

    @GoldenId(value = 22)
    @ApiModelProperty("风速")
    private BigDecimal windSpeed;

    @GoldenId(value = 24)
    @ApiModelProperty("转速")
    private BigDecimal rotateSpeed;


    @GoldenId(value = 21)
    @ApiModelProperty("有功功率")
    private BigDecimal activePower;

    @GoldenId(value = 23)
    @ApiModelProperty("无功功率")
    private BigDecimal reactivePower;

    @GoldenId(value = 177)
    @ApiModelProperty("状态")
    private Integer fanStatus;

    @GoldenId(value = 101)
    @ApiModelProperty("总发电量")
    private BigDecimal energy;

    @ApiModelProperty("日发电量")
    private Double energyForDay;


}