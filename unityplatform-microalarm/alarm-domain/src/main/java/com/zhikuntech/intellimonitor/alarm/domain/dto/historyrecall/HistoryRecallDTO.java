package com.zhikuntech.intellimonitor.alarm.domain.dto.historyrecall;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/7/19 15:11
 * @Description 事故追忆返回数据
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("历史追忆返回数据")
public class HistoryRecallDTO {
    /**
     * 点位id
     */
    @ApiModelProperty("点位id")
    private Integer id;
    /**
     * 时间
     */
    @ApiModelProperty("时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
    /**
     * 数值
     */
    @ApiModelProperty("数值")
    private BigDecimal value;
}
