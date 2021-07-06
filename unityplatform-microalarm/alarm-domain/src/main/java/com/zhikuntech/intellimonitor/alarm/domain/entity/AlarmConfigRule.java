package com.zhikuntech.intellimonitor.alarm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 规则表
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmConfigRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 规则编码
     */
    private String ruleNo;

    /**
     * 告警类型(运行事项/操作记录/系统事件)
     */
    private String alarmType;

    /**
     * 告警规则值
     */
    private String alarmValue;

    /**
     * 一级预警规则值
     */
    private String preAlarmOneValue;

    /**
     * 二级预警规则值
     */
    private String preAlarmTwoValue;

    /**
     * 告警等级编码
     */
    private String levelNoAlarm;

    /**
     * 一级预警等级编码
     */
    private String levelNoOne;

    /**
     * 二级预警等级编码
     */
    private String levelNoTwo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}