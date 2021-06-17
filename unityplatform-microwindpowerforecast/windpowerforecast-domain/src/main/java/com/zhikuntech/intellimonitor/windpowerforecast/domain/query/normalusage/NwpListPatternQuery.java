package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 曲线展示-列表查询
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("曲线展示-列表查询")
public class NwpListPatternQuery {


    @ApiModelProperty("查询模式:[日/月]->[day/month], 目前只支持日查询")
    private String queryMode;

    @ApiModelProperty("日期字符串:[yyyy-MM-dd]")
    private String dateStr;

    @ApiModelProperty("预测高度")
    private String nwpHigh;

    @ApiModelProperty("实测高度")
    private String cfHigh;

    @ApiModelProperty("每页数据数量")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNumber;
}
