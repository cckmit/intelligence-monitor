package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 风玫瑰图列表模式
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("风玫瑰图列表模式")
public class CfListDTO {

    private Integer id;

    @ApiModelProperty("日期")
    private LocalDate date;

    @ApiModelProperty("时间")
    private LocalTime time;

    @ApiModelProperty("实测气象高度（m）")
    private BigDecimal highLevel;

    @ApiModelProperty("实测气象风向（°）")
    private BigDecimal windDirection;

    @ApiModelProperty("实测气象风速（m/s）")
    private BigDecimal windSpeed;

    @ApiModelProperty("实测气象密度（kg/m3)")
    private BigDecimal pressure;

    @ApiModelProperty("功率（kW）")
    private BigDecimal power;

}
