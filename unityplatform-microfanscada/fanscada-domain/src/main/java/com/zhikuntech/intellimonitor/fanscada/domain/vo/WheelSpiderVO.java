package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName: intelligence-monitor
 * @Description:风轮机
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WheelSpiderVO {

    @GoldenId(value = 161)
    @ApiModelProperty("偏航状态")
    private BigDecimal yawingStatus;

    @GoldenId(value = 162)
    @ApiModelProperty("偏航角度")
    private BigDecimal yawingAngle;

    @GoldenId(value = 163)
    @ApiModelProperty("轮毂温度")
    private BigDecimal wheelSpiderTemperature;

    @GoldenId(value = 164)
    @ApiModelProperty("轮毂主轴承温度")
    private BigDecimal wheelmainTemperature;

    @GoldenId(value = 165)
    @ApiModelProperty("液压油温")
    private BigDecimal hydraulicTemperature;



}
