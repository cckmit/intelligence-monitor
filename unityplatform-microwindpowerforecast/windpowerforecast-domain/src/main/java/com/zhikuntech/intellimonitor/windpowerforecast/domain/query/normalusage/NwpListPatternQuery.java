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


    @ApiModelProperty(value = "查询模式:[日/月]->[day/month]", required = true)
    private String queryMode;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd], 月模式查询可以为空", required = true)
    private String dateStrPre;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd]", required = true)
    private String dateStrPost;

    @ApiModelProperty(value = "预测高度", required = true)
    private String nwpHigh;

    @ApiModelProperty(value = "实测高度", required = true)
    private String cfHigh;

    @ApiModelProperty(value = "每页数据数量", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNumber;
}
