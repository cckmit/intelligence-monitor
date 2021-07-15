package com.zhikuntech.intellimonitor.alarm.domain.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测点
 * </p>
 *
 * @author liukai
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmConfigMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测点编码
     */
    @TableId(value = "monitor_no", type = IdType.ASSIGN_ID)
    private String monitorNo;

    /**
     * 测点名称
     */
    private String monitorName;

    /**
     * 规则编码
     */
    private String ruleNo;

    /**
     * 测点类型(0遥信数据/1遥测数据)
     */
    private Integer monitorType;

    /**
     * 每个告警页面会有不同的模块
     */
    private Integer modelType;

    /**
     * 模块名称
     */
    private String modelName;

    /**
     * 分组类型(按照模块进行分组)
     */
    private Integer groupType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 0number1bool
     */
    private Integer valueType;

    /**
     * 测点描述
     */
    private String monitorDescribe;

    /**
     * 告警描述单位需要用到此字段(如果不为空就拼接上该信息)
     */
    private String engineeringUnit;


}
