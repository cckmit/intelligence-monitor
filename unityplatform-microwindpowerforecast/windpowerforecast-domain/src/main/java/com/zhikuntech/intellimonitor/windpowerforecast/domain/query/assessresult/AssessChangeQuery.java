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

    @ApiModelProperty(value = "标识id【wf_assess_day#id】", required = true)
    private Integer id;

    /**
     * 修改原因
     */
    @ApiModelProperty(value = "修改原因")
    private String changeReason;

    // 认证==================================================

    /**
     * 执行人
     */
    @ApiModelProperty(value = "执行人", required = true)
    private String execPerson;

    /**
     * 监护人
     */
    @ApiModelProperty(value = "监护人", required = true)
    private String guardian;

    // 认证==================================================

    // 修改数据(更改后)==================================================

    /**
     * 更改后-短期预测功率漏报次数
     */
    @ApiModelProperty(value = "更改后-短期预测功率漏报次数")
    private Integer dqHiatusChange;

    /**
     * 更改后-短期预测功率准确率（%）
     */
    @ApiModelProperty(value = "更改后-短期预测功率准确率（%）")
    private BigDecimal dqRatioChange;

    /**
     * 更改后-超短期预测功率漏报次数
     */
    @ApiModelProperty(value = "更改后-超短期预测功率漏报次数")
    private Integer cdqHiatusChange;

    /**
     * 更改后-超短期预测功率准确率（%）
     */
    @ApiModelProperty(value = "更改后-超短期预测功率准确率（%）")
    private BigDecimal cdqRatioChange;

    // 修改数据(更改后)==================================================


}
