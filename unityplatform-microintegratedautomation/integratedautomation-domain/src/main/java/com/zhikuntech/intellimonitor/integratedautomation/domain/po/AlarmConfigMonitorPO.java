package com.zhikuntech.intellimonitor.integratedautomation.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/7/22 11:24
 * @Description 告警配置
 * @Version 1.0
 */
@Data
@TableName(value = "alarm_config_monitor")
@ApiModel("告警配置")
public class AlarmConfigMonitorPO {
    /**
     * 测点编码
     */
    @ApiModelProperty("测点编码")
    private String monitorNo;
    /**
     * 测点名称
     */
    @ApiModelProperty("测点名称")
    private String monitorName;
    /**
     * 规则编码
     */
    @ApiModelProperty("规则编码")
    private String ruleNo;
    /**
     * 测点类型(0遥信数据/1遥测数据)
     */
    @ApiModelProperty("测点类型(0遥信数据/1遥测数据)")
    private Integer monitorType;
    /**
     * 每个告警页面会有不同的模块
     */
    @ApiModelProperty("每个告警页面会有不同的模块")
    private Integer modelType;
    /**
     * 模块名称
     */
    @ApiModelProperty("模块名称")
    private String modelName;
    /**
     * 分组类型(按照模块进行分组)
     */
    @ApiModelProperty("分组类型(按照模块进行分组)")
    private Integer groupType;
    /**
     * 分组名称
     */
    @ApiModelProperty("分组名称")
    private String groupName;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 0:number 1:bool
     */
    @ApiModelProperty("0:number 1:bool")
    private Integer valueType;
    /**
     * 测点描述
     */
    @ApiModelProperty("测点描述")
    private String monitorDescribe;
    /**
     * 告警描述单位需要用到此字段(如果不为空就拼接上该信息)
     */
    @ApiModelProperty("告警描述单位需要用到此字段(如果不为空就拼接上该信息)")
    private String engineeringUnit;
    /**
     * 逻辑分区(汇总一个页面的所有测点)
     */
    @ApiModelProperty("逻辑分区(汇总一个页面的所有测点)")
    private String logicPartition;
}
