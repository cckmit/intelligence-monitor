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
 * 
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WfAssessChange implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orgId;

    /**
     * 关联wf_assess_day#id
     */
    private Integer dayRefId;

    /**
     * 计算日期
     */
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
     * 超短期预测功率准确率（%）
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

    /**
     * 执行人
     */
    private String execPerson;

    /**
     * 监护人
     */
    private String guardian;

    /**
     * 修正原因
     */
    private String fixReason;

    /**
     * 修正时间
     */
    private LocalDateTime fixTime;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 是否最新(0 是 1否)
     */
    private Integer newest;

    /**
     * 之前修改的记录
     */
    private Integer preId;


}
