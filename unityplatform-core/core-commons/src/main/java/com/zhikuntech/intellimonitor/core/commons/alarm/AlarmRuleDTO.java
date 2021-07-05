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
public class AlarmRuleDTO {

    private String id;

    /**
     * 告警范围
     */
    @NotNull
    private String alarmRange;

    /**
     * 一级预警范围
     */
    @NotNull
    private String preWarningRangeLevelOne;

    /**
     * 二级预警范围
     */
    @NotNull
    private String preWarningRangeLevelTwe;

    /**
     * 三级预警范围
     */
    @NotNull
    private String preWarningRangeLevelThree;

}
