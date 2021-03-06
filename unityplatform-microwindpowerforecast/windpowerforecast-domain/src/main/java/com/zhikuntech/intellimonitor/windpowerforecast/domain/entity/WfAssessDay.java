package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.*;

/**
 * <p>
 * 日考核结果
 * </p>
 *
 * @author liukai
 * @since 2021-06-22
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class WfAssessDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orgId;

    @Version
    private Integer version;

    private LocalDateTime calcDate;

    /**
     * 短期预测功率漏报次数
     */
    private Integer dqHiatus;

    /**
     * 短期预测功率准确率（%）
     */
    private BigDecimal dqRatio;

    /**
     * 短期预测功率准确率考核电量（MWh）
     */
    private BigDecimal dqElectric;

    /**
     * 短期预测功率准确率考核费用（元）
     */
    private BigDecimal dqPay;

    /**
     * 超短期预测功率漏报次数
     */
    private Integer cdqHiatus;

    /**
     * 短期预测功率准确率（%）
     */
    private BigDecimal cdqRatio;

    /**
     * 短期预测功率准确率考核电量（MWh）
     */
    private BigDecimal cdqElectric;

    /**
     * 超短期预测功率准确率考核费用（元）
     */
    private BigDecimal cdqPay;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 日考核电量
     */
    private BigDecimal dayAssessElectric;


}
