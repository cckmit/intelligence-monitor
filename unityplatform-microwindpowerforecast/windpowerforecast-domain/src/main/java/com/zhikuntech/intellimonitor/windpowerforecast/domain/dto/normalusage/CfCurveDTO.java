package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("风玫瑰图曲线模式")
public class CfCurveDTO {

    @ApiModelProperty("方位范围")
    private String directionName;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("数量占比")
    private BigDecimal numRatio;

    @ApiModelProperty("计算功率")
    private BigDecimal calcPower;

    @ApiModelProperty("计算功率占比")
    private BigDecimal calcPowerRatio;

}
