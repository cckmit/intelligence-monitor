package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 容量
 * </p>
 *
 * @author liukai
 * @since 2021-06-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class WfDataCapacity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orgId;

    /**
     * 事件事件
     */
    private LocalDateTime eventDateTime;

    /**
     * 短期/超短期功率计算时使用的容量
     */
    private BigDecimal powerCalcCapacity;

    /**
     * 考核结果计算时使用的容量
     */
    private BigDecimal checkCalcCapacity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * (数据获取状态)00全场成功, 11 全部失败，01功率计算成功, 10考核结果计算成功
     */
    private String status;


}
