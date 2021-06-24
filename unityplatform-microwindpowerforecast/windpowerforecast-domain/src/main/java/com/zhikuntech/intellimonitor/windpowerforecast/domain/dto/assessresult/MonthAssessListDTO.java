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
 * 月考核结果-列表模式
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("月考核结果-列表模式")
public class MonthAssessListDTO {

    private Integer id;

    /**
     * 计算日期
     */
    @ApiModelProperty("计算日期")
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDateTime calcDate;

    /**
     * 自动核算考核电量（MWh）
     */
    @ApiModelProperty("自动核算考核电量（MWh）")
    private BigDecimal autoElectric;

    /**
     * 自动核算考核费用（元）
     */
    @ApiModelProperty("自动核算考核费用（元）")
    private BigDecimal autoPay;

    /**
     * 最终修改后考核电量（MWh）
     */
    @ApiModelProperty("最终修改后考核电量（MWh）")
    private BigDecimal fnlElectric;

    /**
     * 最终修改后考核费用（元）
     */
    @ApiModelProperty("最终修改后考核费用（元）")
    private BigDecimal fnlPay;

    /**
     * 调度考核电量（MWh）
     */
    @ApiModelProperty("调度考核电量（MWh）")
    private BigDecimal scheduleElectric;

    /**
     * 调度考核费用（元）
     */
    @ApiModelProperty("调度考核费用（元）")
    private BigDecimal schedulePay;

    /**
     * 考核电量对比结果（MWh）
     */
    @ApiModelProperty("考核电量对比结果（MWh）")
    private BigDecimal contrastElectric;

    /**
     * 考核费用对比结果（元）
     */
    @ApiModelProperty("考核费用对比结果（元）")
    private BigDecimal contrastPay;

    /**
     * 最终修改后-考核电量对比结果（MWh）
     */
    @ApiModelProperty("最终修改后-考核电量对比结果（MWh）")
    private BigDecimal fnlContrastElectric;

    /**
     * 最终修改后-考核费用对比结果（元）
     */
    @ApiModelProperty("最终修改后-考核费用对比结果（元）")
    private BigDecimal fnlContrastPay;

    /**
     * 是否有最终修改后的的考核[0有, 1没有]
     */
    @ApiModelProperty("是否有最终修改后的的考核[0有, 1没有]")
    private Integer fnlResult;

}
