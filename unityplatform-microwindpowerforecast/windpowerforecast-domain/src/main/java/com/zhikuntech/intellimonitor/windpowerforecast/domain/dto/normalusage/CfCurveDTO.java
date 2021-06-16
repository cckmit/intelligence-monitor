package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liukai
 */
@Data
@ApiModel("风玫瑰图曲线模式")
public class CfCurveDTO {

    private Integer id;

    @ApiModelProperty("方位范围")
    private String directionName;

    @ApiModelProperty("数量")
    private Integer num;



    @ApiModelProperty("方位名称")
    private BigDecimal calcPower;

}
