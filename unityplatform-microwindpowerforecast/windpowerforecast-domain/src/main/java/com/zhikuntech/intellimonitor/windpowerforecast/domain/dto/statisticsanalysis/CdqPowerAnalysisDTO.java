package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liukai
 */
@Data
public class CdqPowerAnalysisDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日期")
    private LocalDateTime calcDate;

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
