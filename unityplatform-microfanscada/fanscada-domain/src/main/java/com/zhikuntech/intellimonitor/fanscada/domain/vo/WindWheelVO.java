package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName: intelligence-monitor
 * @Description:
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindWheelVO {

    @GoldenId(value = 136)
    @ApiModelProperty("桨距角A")
    private BigDecimal kexAngle_A;

    @GoldenId(value = 137)
    @ApiModelProperty("桨距角B")
    private BigDecimal kexAngle_B;

    @GoldenId(value = 138)
    @ApiModelProperty("桨距角C")
    private BigDecimal kexAngle_C;


    @GoldenId(value = 139)
    @ApiModelProperty("风轮转速")
    private BigDecimal ratedRotSpeed;
    @GoldenId(value = 140)
    @ApiModelProperty("有功功率")
    private BigDecimal activepower;

    @GoldenId(value = 141)
    @ApiModelProperty("无功功率")
    private BigDecimal reactivepower;

    @GoldenId(value = 142)
    @ApiModelProperty("风机频率")
    private BigDecimal fqcy;

    @GoldenId(value = 143)
    @ApiModelProperty("风机风速")
    private BigDecimal windspeed;

    @GoldenId(value = 144)
    @ApiModelProperty("风向")
    private BigDecimal winddirection;

    @GoldenId(value = 146)
    @ApiModelProperty("故障代码")
    private BigDecimal errorCode;

    @GoldenId(value = 135)
    @ApiModelProperty("功率因数")
    private BigDecimal powernum;

    @GoldenId(value = 157)
    @ApiModelProperty("U相电压")
    private BigDecimal uphaseV;

    @GoldenId(value = 158)
    @ApiModelProperty("V相电压")
    private BigDecimal vphaseV;

    @GoldenId(value = 159)
    @ApiModelProperty("W相电压")
    private BigDecimal wphaseV;

    @GoldenId(value = 150)
    @ApiModelProperty("U相电流")
    private BigDecimal uphaseA;

    @GoldenId(value = 151)
    @ApiModelProperty("V相电流")
    private BigDecimal vphaseA;

    @GoldenId(value = 152)
    @ApiModelProperty("W相电流")
    private BigDecimal wphaseA;


}
