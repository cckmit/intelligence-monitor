package com.zhikuntech.intellimonitor.structuremonitor.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @author liukai
 */
@Data
@ApiModel("结构监测查询")
public class StructureMonitoringQuery {

    @ApiModelProperty(value = "查询模式:[日/月]->[day/month]", required = true)
    private String queryMode;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd]", required = true)
    private String dateStrPre;

    @ApiModelProperty(value = "日期字符串:[yyyy-MM-dd]", required = true)
    private String dateStrPost;

    @ApiModelProperty(value = "每页数据数量，默认为10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "页码，默认为1")
    private Integer pageNumber = 1;

    @ApiModelProperty(value = "1:全部，2:振动数据&加速度，3:不均匀沉降&加速度，默认为1", required = false)
    private Integer dataType = 1;

    @ApiModelProperty(value = "1:分钟最大值，2:分钟平均值，3:分钟最小值，默认为1", required = false)
    private Integer bookmark = 1;

}
