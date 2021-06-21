package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测风曲线模式查询
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("测风曲线模式查询")
public class CfCurvePatternQuery {

    @ApiModelProperty(value = "查询模式:[日/月]->[day/month], 目前只支持日查询", required = true)
    private String queryMode;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd]", required = true)
    private String dateStrPre;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd]", required = true)
    private String dateStrPost;

    @ApiModelProperty(value = "高度", required = true)
    private String high;
}
