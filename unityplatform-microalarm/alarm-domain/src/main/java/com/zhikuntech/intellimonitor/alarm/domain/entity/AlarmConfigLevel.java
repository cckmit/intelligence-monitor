package com.zhikuntech.intellimonitor.alarm.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

/**
 * <p>
 * 告警等级表
 * </p>
 *
 * @author liukai
 * @since 2021-07-08
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmConfigLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 等级编码
     */
    @TableId(value = "level_no", type = IdType.ASSIGN_ID)
    private String levelNo;

    /**
     * 告警等级(1/2/3)
     */
    private Integer alarmLevel;

    /**
     * 告警说明(重要/一般/告知)
     */
    private String levelIllustrate;

    /**
     * 告警方式(闪烁/弹窗)
     */
    private String alarmWay;

    /**
     * 备注
     */
    private String mark;


}
