package com.zhikuntech.intellimonitor.core.commons.alarm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 分组枚举
 *
 * @author liukai
 * @date 2021/07/05
 */
@RequiredArgsConstructor
public enum MonitorPointGroupEnum {

    /**
     * 综合自动化对应枚举
     */
    INTEGRATED_AUTO(1, "综合自动化"),
    // TODO ...
    ;

    /**
     * 测点组值
     */
    @Getter
    private final Integer groupType;

    /**
     * 对应模块名称
     * <p>
     *     1.综合自动化
     *     2.风机SCADA
     *     3.风功率预测
     *     4.海缆监测
     *     5.SVG
     *     6.AGC/AVC
     *     7.设备在线监测
     *     8.暖通空调
     *     9.火灾报警
     *     10.机组升压设备监控
     *     11.结构监测
     * </p>
     */
    @Getter
    private final String moduleName;
}
