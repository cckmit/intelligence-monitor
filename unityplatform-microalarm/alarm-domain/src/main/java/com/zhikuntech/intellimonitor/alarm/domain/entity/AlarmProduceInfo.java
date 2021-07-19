package com.zhikuntech.intellimonitor.alarm.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.*;

/**
 * <p>
 * 告警信息
 * </p>
 *
 * @author liukai
 * @since 2021-07-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmProduceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Version
    private Integer version;

    /**
     * 告警信息编码
     */
    @TableId(value = "info_no", type = IdType.ASSIGN_ID)
    private String infoNo;

    /**
     * 测点编码
     */
    private String monitorNo;

    /**
     * 页面分组类型(同alarm_config_monitor#group_type)
     */
    private Integer groupType;

    /**
     * 规则编码
     */
    private String ruleNo;

    /**
     * 测点产生数据时间
     */
    private LocalDateTime eventTime;

    /**
     * 测点处理时间
     */
    private LocalDateTime processTime;

    /**
     * 测点值
     */
    private BigDecimal monitorNum;

    /**
     * 测点值字符串
     */
    private String monitorNumStr;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 0 非历史 1历史
     */
    private Integer withHistory;

    /**
     * 上一个状态编码
     */
    private String preInfoNo;

    /**
     * 下一条状态编码
     */
    private String nextInfoNo;

    /**
     * 是否已确认(0未确认1已确认)
     */
    private Integer hasConfirm;

    /**
     * 确认人
     */
    private String confirmPerson;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 是否已恢复(即告警是否已解除,0未解除1已解除)
     */
    private Integer hasRestore;

    /**
     * 恢复时间
     */
    private LocalDateTime restoreTime;

    /**
     * 告警产生日期(同createTime)
     */
    private LocalDateTime alarmDate;

    /**
     * 告警产生时间戳
     */
    private LocalDateTime alarmTimestamp;

    /**
     * 行号(唯一不重复)
     */
    private Long rowStamp;

    /**
     * 告警描述
     */
    private String alarmDescribe;

    /**
     * 告警等级(严重/一般/告知)
     */
    private String alarmLevel;


}
