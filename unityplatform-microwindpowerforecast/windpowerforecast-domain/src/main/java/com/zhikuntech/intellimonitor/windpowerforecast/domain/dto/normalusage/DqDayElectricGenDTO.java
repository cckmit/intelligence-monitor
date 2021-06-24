package com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class DqDayElectricGenDTO {

    @JsonFormat(pattern = "MM月dd日")
    @ApiModelProperty("日期")
    private LocalDateTime date;

    /**
     * 计算使用日期
     */
    @JsonIgnore
    private LocalDate calcUseDate;

    @ApiModelProperty("发电量")
    private BigDecimal electricGen;

}
