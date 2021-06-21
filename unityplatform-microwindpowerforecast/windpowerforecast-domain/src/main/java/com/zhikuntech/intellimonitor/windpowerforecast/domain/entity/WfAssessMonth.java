package com.zhikuntech.intellimonitor.windpowerforecast.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 月考核结果
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WfAssessMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orgId;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 计算日期
     */
    private LocalDateTime calcDate;

    /**
     * 自动核算考核电量（MWh）
     */
    private BigDecimal autoElectric;

    /**
     * 自动核算考核费用（元）
     */
    private BigDecimal autoPay;

    /**
     * 最终修改后考核电量（MWh）
     */
    private BigDecimal fnlElectric;

    /**
     * 最终修改后考核费用（元）
     */
    private BigDecimal fnlPay;

    /**
     * 调度考核电量（MWh）
     */
    private BigDecimal scheduleElectric;

    /**
     * 调度考核费用（元）
     */
    private BigDecimal schedulePay;

    /**
     * 考核电量对比结果（MWh）
     */
    private BigDecimal contrastElectric;

    /**
     * 考核费用对比结果（元）
     */
    private BigDecimal contrastPay;

    /**
     * 最终修改后-考核电量对比结果（MWh）
     */
    private BigDecimal fnlContrastElectric;

    /**
     * 最终修改后-考核费用对比结果（元）
     */
    private BigDecimal fnlContrastPay;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
