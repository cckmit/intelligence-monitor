package com.zhikuntech.intellimonitor.alarm.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liukai
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public enum  AlarmLevelEnum {

    /**
     * 重要
     */
    IMPORTANT(1, "重要"),

    /**
     * 一般
     */
    NORAML(2, "一般"),

    /**
     * 告知
     */
    INFO(3, "告知"),
    ;

    /**
     * 告警等级
     */
    @Getter
    private final Integer level;

    /**
     * 等级说明
     */
    @Getter
    private final String levelIllustrate;

}
