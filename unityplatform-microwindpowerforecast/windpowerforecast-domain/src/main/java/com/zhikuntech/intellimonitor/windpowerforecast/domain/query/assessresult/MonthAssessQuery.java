package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 月评估查询
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MonthAssessQuery {

    /**
     * 查询某一年
     */
    public static final String QUERY_ONE = "one";

    @ApiModelProperty(value = "查询模式[查询某一年(one)/查询范围(range)]", required = true)
    private String queryMod;

    @ApiModelProperty(value = "查询年份[前]", required = true)
    private String queryYearPre;

    @ApiModelProperty(value = "查询年份[后], 模式为one时为空", required = true)
    private String queryYearPost;

    @ApiModelProperty(value = "每页数据数量", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNumber;

}
