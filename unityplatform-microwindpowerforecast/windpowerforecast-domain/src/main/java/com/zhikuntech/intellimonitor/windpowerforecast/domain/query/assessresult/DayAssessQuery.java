package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("日考核-查询")
public class DayAssessQuery {

    @ApiModelProperty(value = "查询日期,格式[yyyy-MM]", required = true)
    private String date;

    @ApiModelProperty(value = "每页数据数量", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNumber;

    @ApiModelProperty(value = "排序字段", required = true)
    private String oderByField;

    @ApiModelProperty(value = "生序(up)or降序(down)", required = true)
    private String upOrDown;
}
