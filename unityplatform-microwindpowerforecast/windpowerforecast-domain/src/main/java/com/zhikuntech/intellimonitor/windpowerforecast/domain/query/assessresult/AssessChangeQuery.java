package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 考核结果评估修改
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("评估修改接口")
public class AssessChangeQuery {

    private int id;

    /**
     * 修改原因
     */
    @ApiModelProperty("修改原因")
    private String changeReason;

    // 认证==================================================

    /**
     * 执行人
     */
    @ApiModelProperty("执行人")
    private String execPerson;

    /**
     * 执行人-密码
     */
    @ApiModelProperty("执行人-密码")
    private String execPersonSec;

    /**
     * 监护人
     */
    @ApiModelProperty("监护人")
    private String guardian;

    /**
     * 监护人-密码
     */
    @ApiModelProperty("监护人-密码")
    private String guardianSec;

    // 认证==================================================

    // 修改数据(初始数据)==================================================

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
     * 超短期预测功率漏报次数
     */
    @ApiModelProperty("超短期预测功率漏报次数")
    private Integer cdqHiatus;

    /**
     * 超短期预测功率准确率（%）
     */
    @ApiModelProperty("超短期预测功率准确率（%）")
    private BigDecimal cdqRatio;

    // 修改数据(初始数据)==================================================

    // 修改数据(更改后)==================================================

    /**
     * 更改后-短期预测功率漏报次数
     */
    @ApiModelProperty("更改后-短期预测功率漏报次数")
    private Integer dqHiatusChange;

    /**
     * 更改后-短期预测功率准确率（%）
     */
    @ApiModelProperty("更改后-短期预测功率准确率（%）")
    private BigDecimal dqRatioChange;

    /**
     * 更改后-超短期预测功率漏报次数
     */
    @ApiModelProperty("更改后-超短期预测功率漏报次数")
    private Integer cdqHiatusChange;

    /**
     * 更改后-超短期预测功率准确率（%）
     */
    @ApiModelProperty("更改后-超短期预测功率准确率（%）")
    private BigDecimal cdqRatioChange;

    // 修改数据(更改后)==================================================


}
