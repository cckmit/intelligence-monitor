package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 日考核结果
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("日考核结果")
public class DayAssessListDTO {


    private Integer id;

    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime calcDate;

    //  ====================================================== 未修改

    /**
     * 短期预测功率漏报次数
     */
    @ApiModelProperty("短期预测功率漏报次数")
    private Integer dqHiatus;

    /**
     * 短期预测功率准确率（%）
     */
    @ApiModelProperty("短期预测功率准确率（%）")
    private BigDecimal dqRatio;

    /**
     * 短期预测功率准确率考核电量（MWh）
     */
    @ApiModelProperty("短期预测功率准确率考核电量（MWh）")
    private BigDecimal dqElectric;

    /**
     * 短期预测功率准确率考核费用（元）
     */
    @ApiModelProperty("短期预测功率准确率考核费用（元）")
    private BigDecimal dqPay;


    /**
     * 超短期预测功率漏报次数
     */
    @ApiModelProperty("超短期预测功率漏报次数")
    private Integer cdqHiatus;

    /**
     * 短期预测功率准确率（%）
     */
    @ApiModelProperty("短期预测功率准确率（%）")
    private BigDecimal cdqRatio;

    /**
     * 短期预测功率准确率考核电量（MWh）
     */
    @ApiModelProperty("短期预测功率准确率考核电量（MWh）")
    private BigDecimal cdqElectric;

    /**
     * 超短期预测功率准确率考核费用（元）
     */
    @ApiModelProperty("超短期预测功率准确率考核费用（元）")
    private BigDecimal cdqPay;

    //  ====================================================== 未修改


    //  ====================================================== 最终修改

    /**
     * 最终修改-短期预测功率漏报次数
     */
    @ApiModelProperty("最终修改-短期预测功率漏报次数")
    private Integer dqHiatusFnl;

    /**
     * 最终修改-短期预测功率准确率（%）
     */
    @ApiModelProperty("最终修改-短期预测功率准确率（%）")
    private BigDecimal dqRatioFnl;

    /**
     * 最终修改-短期预测功率准确率考核电量（MWh）
     */
    @ApiModelProperty("最终修改-短期预测功率准确率考核电量（MWh）")
    private BigDecimal dqElectricFnl;

    /**
     * 最终修改-短期预测功率准确率考核费用（元）
     */
    @ApiModelProperty("最终修改-短期预测功率准确率考核费用（元）")
    private BigDecimal dqPayFnl;


    /**
     * 最终修改-超短期预测功率漏报次数
     */
    @ApiModelProperty("最终修改-超短期预测功率漏报次数")
    private Integer cdqHiatusFnl;

    /**
     * 最终修改-短期预测功率准确率（%）
     */
    @ApiModelProperty("最终修改-短期预测功率准确率（%）")
    private BigDecimal cdqRatioFnl;

    /**
     * 最终修改-短期预测功率准确率考核电量（MWh）
     */
    @ApiModelProperty("最终修改-短期预测功率准确率考核电量（MWh）")
    private BigDecimal cdqElectricFnl;

    /**
     * 最终修改-超短期预测功率准确率考核费用（元）
     */
    @ApiModelProperty("最终修改-超短期预测功率准确率考核费用（元）")
    private BigDecimal cdqPayFnl;

    //  ====================================================== 最终修改

    /**
     * 执行人
     */
    @ApiModelProperty("执行人")
    private String execPerson;

    /**
     * 监护人
     */
    @ApiModelProperty("监护人")
    private String guardian;

    /**
     * 修正原因
     */
    @ApiModelProperty("修正原因")
    private String fixReason;

    /**
     * 修正时间
     */
    @ApiModelProperty("修正时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fixTime;


}
