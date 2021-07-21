package com.zhikuntech.intellimonitor.structuremonitor.domain.vo;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 滕楠
 * @className LiveData
 * @create 2021/7/16 17:20
 **/
@ApiModel(description = "加速度数据返回实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveSpeedData extends Object{

    @ApiModelProperty("测点1x加速度")
    @GoldenId(value = 1)
    private Long a1xAcceleration;

    @GoldenId(value = 2)
    @ApiModelProperty("测点1y加速度")
    private Long a1yAcceleration;

    @GoldenId(value = 3)
    @ApiModelProperty("测点2x加速度")
    private Long a2xAcceleration;

    @GoldenId(value = 4)
    @ApiModelProperty("测点2y加速度")
    private Long a2yAcceleration;

    @GoldenId(value = 5)
    @ApiModelProperty("测点3x加速度")
    private Long a3xAcceleration;

    @GoldenId(value = 6)
    @ApiModelProperty("测点3y加速度")
    private Long a3yAcceleration;

    @GoldenId(value = 7)
    @ApiModelProperty("测点4x加速度")
    private Long a4xAcceleration;

    @GoldenId(value = 8)
    @ApiModelProperty("测点4y加速度")
    private Long a4yAcceleration;

    @GoldenId(value = 9)
    @ApiModelProperty("测点5x加速度")
    private Long a5xAcceleration;

    @GoldenId(value = 10)
    @ApiModelProperty("测点5y加速度")
    private Long a5yAcceleration;

    @GoldenId(value = 11)
    @ApiModelProperty("测点6x加速度")
    private Long a6xAcceleration;

    @GoldenId(value = 12)
    @ApiModelProperty("测点6y加速度")
    private Long a6yAcceleration;
}