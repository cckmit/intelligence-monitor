package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liukai
 */
@Data
@ApiModel("功率分析查询")
public class PowerAnalysisQuery {


    @ApiModelProperty(value = "查询模式:[日/月]->[day/month], 目前只支持日查询", required = true)
    private String queryMode;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd]", required = true)
    private String dateStrPre;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd]", required = true)
    private String dateStrPost;

    @ApiModelProperty(value = "每页数据数量", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNumber;

}
