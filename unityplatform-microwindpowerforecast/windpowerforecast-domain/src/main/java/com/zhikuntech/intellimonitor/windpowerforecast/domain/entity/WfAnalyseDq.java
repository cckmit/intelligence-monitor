package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 短期功率分析
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class WfAnalyseDq implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 计算日期
     */
    private LocalDateTime calcDate;

    /**
     * 均方根误差
     */
    private BigDecimal avgRmse;

    /**
     * 平均绝对误差
     */
    private BigDecimal avgMae;

    /**
     * 最大预测误差
     */
    private BigDecimal biggestDiff;

    /**
     * 相关系数
     */
    private BigDecimal aboutR;

    /**
     * 准确率
     */
    private BigDecimal r1Ratio;

    /**
     * 合格率
     */
    private BigDecimal r2Ratio;


}
