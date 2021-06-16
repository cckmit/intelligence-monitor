package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测风-列表模式查询
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("风玫瑰图-列表模式查询")
public class CfListPatternQuery {

    @ApiModelProperty("查询模式:[日/月]->[day/month], 目前只支持日查询")
    private String queryMode;

    @ApiModelProperty("日期字符串:[yyyy-MM-dd]")
    private String dateStr;

    @ApiModelProperty("高度")
    private String high;

    @ApiModelProperty("每页数据数量")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNumber;
}
