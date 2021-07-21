package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event;

import org.springframework.context.ApplicationEvent;

/**
 * 告警恢复事件
 *
 * @author liukai
 */
public class AlarmRelieveEvent extends ApplicationEvent {

    private static final long serialVersionUID = -6574990010319407424L;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AlarmRelieveEvent(Object source) {
        super(source);
    }
}
