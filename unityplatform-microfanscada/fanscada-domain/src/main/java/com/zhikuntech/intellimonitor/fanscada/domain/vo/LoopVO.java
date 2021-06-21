package com.zhikuntech.intellimonitor.fanscada.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 滕楠
 * @className Loop
 * @create 2021/6/15 14:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "回路")
public class LoopVO {

    @ApiModelProperty("回路")
    private String loopNumber;

    @JsonIgnore
    @ApiModelProperty("开关位置")
    private String switchLocation;
    @JsonIgnore
    @ApiModelProperty("日发电量")
    private BigDecimal generatingCapacityForDay;
    @JsonIgnore
    @ApiModelProperty("平均风速")
    private BigDecimal windSpeedAvg;
    @JsonIgnore
    @ApiModelProperty("有功功率")
    private BigDecimal activePower;
    @JsonIgnore
    @ApiModelProperty("无功功率")
    private BigDecimal reactivePower;

    @ApiModelProperty("风机列表")
    private List<FanBaseInfoVO> fanBaseInfoVOS;
}