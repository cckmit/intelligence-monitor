package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 日发电量预测
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("日发电量预测")
public class NwpDayElectricGenDTO {

    @ApiModelProperty("日期")
    private LocalDateTime date;

    @ApiModelProperty("发电量")
    private BigDecimal electricGen;

}
