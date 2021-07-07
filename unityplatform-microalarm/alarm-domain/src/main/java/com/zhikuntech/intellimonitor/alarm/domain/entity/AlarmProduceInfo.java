package com.zhikuntech.intellimonitor.alarm.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 告警信息
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmProduceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer version;

    /**
     * 告警信息编码
     */
    private String infoNo;

    /**
     * 测点编码
     */
    private String monitorNo;

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


}
