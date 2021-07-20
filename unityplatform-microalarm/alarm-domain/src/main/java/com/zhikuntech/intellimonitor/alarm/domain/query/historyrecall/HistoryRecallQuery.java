package com.zhikuntech.intellimonitor.alarm.domain.query.historyrecall;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/7/19 14:30
 * @Description 历史追忆查询条件
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("历史追忆查询条件")
public class HistoryRecallQuery {
    /**
     * 点位id
     */
    @ApiModelProperty(value = "点位id",required = true)
    @NotNull(message = "点位id不能为空")
    private Integer id;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间",required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "开始时间不能为空")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间",required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;
}
