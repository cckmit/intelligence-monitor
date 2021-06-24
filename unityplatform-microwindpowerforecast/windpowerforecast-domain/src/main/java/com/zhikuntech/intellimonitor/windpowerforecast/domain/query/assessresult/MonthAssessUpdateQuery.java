package com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 月考核电量更新
 *
 * @author liukai
 */
@Data
@ApiModel("月考核电量更新")
public class MonthAssessUpdateQuery {

    @ApiModelProperty(value = "标识id", required = true)
    private Integer id;

    /**
     * 调度考核电量（MWh）
     */
    @ApiModelProperty(value = "标识id")
    private BigDecimal scheduleElectric;

    /**
     * 调度考核费用（元）
     */
    @ApiModelProperty(value = "标识id")
    private BigDecimal schedulePay;

}
