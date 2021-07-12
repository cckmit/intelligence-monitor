package com.zhikuntech.intellimonitor.alarm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liukai
 */
@Data
@ApiModel("测点信息")
public class AlarmMonitorDTO implements Serializable {

    private static final long serialVersionUID = -272530814538918970L;

    @ApiModelProperty("测点编码")
    private String monitorNo;

    /**
     * 规则编码
     */
    @ApiModelProperty("测点编码")
    private String ruleNo;

    /**
     * 测点类型
     */
    @ApiModelProperty("测点类型")
    private Integer monitorType;

    /**
     * 分组类型(按照模块进行分组)
     */
    @ApiModelProperty("分组类型(按照模块进行分组)")
    private Integer groupType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    /**
     * 测点描述
     */
    @ApiModelProperty("测点描述")
    private String monitorDescribe;

    /**
     * 0number1bool
     */
    @JsonIgnore
    @ApiModelProperty("0number1bool")
    private Integer valueType;

}
