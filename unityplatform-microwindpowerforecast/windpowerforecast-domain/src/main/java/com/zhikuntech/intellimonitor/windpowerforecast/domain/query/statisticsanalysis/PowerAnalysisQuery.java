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


    @ApiModelProperty("查询模式:[日/月]->[day/month], 目前只支持日查询")
    private String queryMode;

    @ApiModelProperty("日期字符串:[yyyy-MM-dd]")
    private String dateStrPre;

    @ApiModelProperty("日期字符串:[yyyy-MM-dd]")
    private String dateStrPost;

    @ApiModelProperty("每页数据数量")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNumber;

}
