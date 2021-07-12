package com.zhikuntech.intellimonitor.onlinemonitor.domain.dto;

import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 代志豪
 * 2021/7/9 10:46
 */
@Data
@ApiModel("变压器实时数据")
public class TransformerRuntimeDTO {

    @ApiModelProperty("变压器编号")
    private Integer num;

    @GoldenId(value = 1)
    @ApiModelProperty("甲烷")
    private Double ch4;

    @GoldenId(value = 2)
    @ApiModelProperty("乙炔")
    private Double c2h2;

    @GoldenId(value = 3)
    @ApiModelProperty("乙烯")
    private Double c2h4;

    @GoldenId(value = 4)
    @ApiModelProperty("乙烷")
    private Double c2h6;

    @GoldenId(value = 5)
    @ApiModelProperty("一氧化碳")
    private Double co;

    @GoldenId(value = 6)
    @ApiModelProperty("二氧化碳")
    private Double co2;

    @GoldenId(value = 7)
    @ApiModelProperty("氧气")
    private Double o2;

    @GoldenId(value = 8)
    @ApiModelProperty("氢气")
    private Double h2;

    @GoldenId(value = 9)
    @ApiModelProperty("氮气")
    private Double n2;

    @GoldenId(value = 10)
    @ApiModelProperty("微水")
    private Double ppm;

    @GoldenId(value = 11)
    @ApiModelProperty("总溶解可燃气体")
    private Long combustibleGas;

    @GoldenId(value = 12)
    @ApiModelProperty("总溶解气体")
    private Long dissolvedGas;

    @GoldenId(value = 13)
    @ApiModelProperty("油压")
    private Double oilPressure;

    @GoldenId(value = 14)
    @ApiModelProperty("油温")
    private Double oilTemp;

    @GoldenId(value = 15)
    @ApiModelProperty("环境温度")
    private Double ambientTemp;

    @GoldenId(value = 16)
    @ApiModelProperty("标准温度")
    private Double standardTemp;

    @GoldenId(value = 17)
    @ApiModelProperty("甲烷")
    private Double humidity;
}
