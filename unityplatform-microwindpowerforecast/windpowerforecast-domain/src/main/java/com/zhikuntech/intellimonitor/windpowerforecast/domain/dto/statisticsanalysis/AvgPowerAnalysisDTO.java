package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("[均值]功率分析")
public class AvgPowerAnalysisDTO {

    /**
     * 均方根误差
     */
    @ApiModelProperty("[平均]均方根误差")
    private BigDecimal avgRmseAvg;

    /**
     * 平均绝对误差
     */
    @ApiModelProperty("[平均]平均绝对误差")
    private BigDecimal avgMaeAvg;

    /**
     * 最大预测误差
     */
    @ApiModelProperty("[平均]最大预测误差")
    private BigDecimal biggestDiffAvg;

    /**
     * 相关系数
     */
    @ApiModelProperty("[平均]相关系数")
    private BigDecimal aboutRAvg;

    /**
     * 准确率
     */
    @ApiModelProperty("[平均]准确率")
    private BigDecimal r1RatioAvg;

    /**
     * 合格率
     */
    @ApiModelProperty("[平均]合格率")
    private BigDecimal r2RatioAvg;
}
