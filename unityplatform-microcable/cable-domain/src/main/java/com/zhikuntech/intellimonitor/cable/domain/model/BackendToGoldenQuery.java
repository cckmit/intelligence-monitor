package com.zhikuntech.intellimonitor.cable.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("BackendToGolden查询条件")
public class BackendToGoldenQuery {
    /**
     * 映射关系中mysql数据库表中编号
     */
    @ApiModelProperty("映射关系中mysql数据库表中编号")
    private Integer backendId;

    /**
     * 风机编号
     */
    @ApiModelProperty("风机编号")
    private Integer number;
}
