package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("短期预测功率分析")
public class DqPowerAnalysisDTO {

    @ApiModelProperty("日期")
    private LocalDateTime date;

    /**
     * 均方根误差
     */
    @ApiModelProperty("均方根误差")
    private BigDecimal avgRmse;

    /**
     * 平均绝对误差
     */
    @ApiModelProperty("平均绝对误差")
    private BigDecimal avgMae;

    /**
     * 最大预测误差
     */
    @ApiModelProperty("最大预测误差")
    private BigDecimal biggestDiff;

    /**
     * 相关系数
     */
    @ApiModelProperty("相关系数")
    private BigDecimal aboutR;

    /**
     * 准确率
     */
    @ApiModelProperty("准确率")
    private BigDecimal r1Ratio;

    /**
     * 合格率
     */
    @ApiModelProperty("合格率")
    private BigDecimal r2Ratio;

}
