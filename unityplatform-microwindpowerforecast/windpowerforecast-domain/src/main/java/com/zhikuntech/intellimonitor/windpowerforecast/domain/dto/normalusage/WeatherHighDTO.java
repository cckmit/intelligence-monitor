package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liukai
 */
@ApiModel("实测气象高度, 预测气象高度")
@Data
public class WeatherHighDTO {

    @ApiModelProperty("实测气象高度")
    private List<BigDecimal> actHigh;

    @ApiModelProperty("预测气象高度")
    private List<BigDecimal> virtualHigh;

}
