package com.zhikuntech.intellimonitor.alarm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测点
 * </p>
 *
 * @author liukai
 * @since 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmConfigMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测点编码
     */
    private String monitorNo;

    /**
     * 规则编码
     */
    private String ruleNo;

    /**
     * 测点类型
     */
    private Integer monitorType;

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


}
