package com.zhikuntech.intellimonitor.cable.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("mysql数据库和golden数据库关于点位的映射关系")
public class BackendToGolden {
    /**
     * 表id
     */
    @ApiModelProperty("表id")
    private Integer id;

    /**
     * 映射关系中mysql数据库表中编号
     */
    @ApiModelProperty("映射关系中mysql数据库表中编号")
    private Integer backendId;

    /**
     * 映射关系中golden数据库表中id
     */
    @ApiModelProperty("映射关系中golden数据库表中id")
    private Integer goldenId;

    /**
     * 风机编号
     */
    @ApiModelProperty("海缆编号")
    private Integer number;

    /**
     * 描述信息
     */
    @ApiModelProperty("描述信息")
    private String description;

    /**
     * 标签点名称
     */
    @ApiModelProperty("标签点名称")
    private String tagName;

}
