package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis;

import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liukai
 */
@Data
@ApiModel("[聚合]超短期预测功率分析")
public class CdqListAggregateDTO {

    @ApiModelProperty("分页结果")
    private Pager<CdqPowerAnalysisDTO> pager;

    @ApiModelProperty("[均值]功率分析")
    private AvgPowerAnalysisDTO avgAna;

}
