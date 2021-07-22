package com.zhikuntech.intellimonitor.structuremonitor.domain.vo;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 滕楠
 * @className LiveSedimentationData  沉降数据实体类
 * @create 2021/7/20 17:58
 **/
@ApiModel(value = "沉降数据",description = "沉降数据返回实体类")
@Data
public class LiveSedimentationData {

    private Date dataTime;

    private int fanNumber;
    @GoldenId(value = 13)
    @ApiModelProperty("1沉降")
    private BigDecimal a1Subside;

    @GoldenId(value = 14)
    @ApiModelProperty("2沉降")
    private BigDecimal a2Subside;

    @GoldenId(value = 15)
    @ApiModelProperty("3沉降")
    private BigDecimal a3Subside;

    @GoldenId(value = 16)
    @ApiModelProperty("4沉降")
    private BigDecimal a4Subside;

    @ApiModelProperty("1沉降计算值")
    private BigDecimal a1SubsideCalculate;

    @ApiModelProperty("2沉降计算值")
    private BigDecimal a2SubsideCalculate;

    @ApiModelProperty("3沉降计算值")
    private BigDecimal a3SubsideCalculate;

    @ApiModelProperty("4沉降计算值")
    private BigDecimal a4SubsideCalculate;

    @ApiModelProperty("2沉降相对值")
    private BigDecimal a2SubsideToa1;

    @ApiModelProperty("3沉降相对值")
    private BigDecimal a3SubsideToa1;

    @ApiModelProperty("4沉降相对值")
    private BigDecimal a4SubsideToa1;


}