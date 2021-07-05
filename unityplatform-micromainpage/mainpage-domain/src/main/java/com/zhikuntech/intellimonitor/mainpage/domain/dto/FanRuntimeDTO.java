package com.zhikuntech.intellimonitor.mainpage.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhikuntech.intellimonitor.core.commons.golden.annotation.GoldenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 代志豪
 * @date 2021-06-07
 */

@Data
@Accessors(chain = true)
@ApiModel("首页风机运行实时数据")
public class FanRuntimeDTO {

    /**
     * 风机编号
     */
    @ApiModelProperty("风机编号")
    @ExcelProperty(value = "风机编号", index = 0)
    private Integer number;

    /**
     * 风速
     */
    @GoldenId(value = 1)
    @ApiModelProperty("风速")
    @ExcelProperty(value = "风速(m/s)", index = 1)
    private Double windVelocity;

    /**
     * 有功功率
     */
    @GoldenId(value = 2)
    @ApiModelProperty("有功功率")
    @ExcelProperty(value = "功率(kW)", index = 2)
    private Double activePower;

    /**
     * 发电机转速
     */
    @GoldenId(value = 3)
    @ApiModelProperty("发电机转速")
    @ExcelProperty(value = "发电机转速(rpm)", index = 3)
    private Double generatorRate;

    /**
     * 月发电量
     */
    @GoldenId(value = 4)
    @ApiModelProperty("月发电量")
    @ExcelProperty(value = "月总发电量(万kWh)", index = 4)
    private Double monthlyPowerGeneration;

    /**
     * 环境温度
     */
    @GoldenId(value = 5)
    @ApiModelProperty("环境温度")
    @ExcelProperty(value = "环境温度(℃)", index = 5)
    private Double ambientTemp;

    /**
     * 前轴承温度
     */
    @GoldenId(value = 6)
    @ApiModelProperty("前轴承温度")
    @ExcelProperty(value = "前轴承温度(℃)", index = 6)
    private Double frontBearingTemp;

    /**
     * 后轴承温度
     */
    @GoldenId(value = 7)
    @ApiModelProperty("后轴承温度")
    @ExcelProperty(value = "后轴承温度(℃)", index = 7)
    private Double rearBearingTemp;

    /**
     * 齿轮主轴承温度
     */
    @GoldenId(value = 8)
    @ApiModelProperty("齿轮主轴承温度")
    @ExcelProperty(value = "齿轮轴承温度(℃)", index = 8)
    private Double gearBearingTemp;

    /**
     * 变压器室温
     */
    @GoldenId(value = 9)
    @ApiModelProperty("机舱温度")
    @ExcelProperty(value = "机舱温度(℃)", index = 9)
    private Double transformerTemp;

    /**
     * 变压器油温
     */
    @GoldenId(value = 10)
    @ApiModelProperty("变压器油温")
    @ExcelProperty(value = "变压器油温(℃)", index = 10)
    private Double transformerOilTemp;

    /**
     * 故障代码
     */
    @GoldenId(value = 11)
    @ApiModelProperty("故障代码")
    @ExcelProperty(value = "故障代码", index = 11)
    private Long errorCode;

    /**
     * 运行状态
     */
    @GoldenId(value = 12)
    @ApiModelProperty("运行状态")
    @ExcelIgnore
    private Long runningStatus;

    /**
     * 运行状态图片uri
     */
    @ExcelProperty(value = "运行状态", index = 12)
    @JsonIgnore
    private String runningStatusString;

}
