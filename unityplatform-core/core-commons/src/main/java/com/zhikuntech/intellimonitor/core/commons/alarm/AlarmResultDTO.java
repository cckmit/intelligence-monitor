package com.zhikuntech.intellimonitor.core.commons.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AlarmResultDTO {

    private String id;

    /**
     * 0 不产生告警
     * 1 产生告警
     */
    @NotNull
    private Integer produce;

    /**
     * 1 告警
     * 2 一级预警
     * 3 二级预警
     * ....
     */
    @NotNull
    private Integer level;

}
