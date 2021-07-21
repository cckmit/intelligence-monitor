package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * 告警恢复事件
 *
 * @author liukai
 */
public class AlarmRelieveEvent extends ApplicationEvent {

    private static final long serialVersionUID = -6574990010319407424L;

    /**
     * 测点编码
     */
    @Getter
    private final String monitorNo;

    /**
     * 告警链编码
     */
    @Getter
    private final String chainInfo;

    public AlarmRelieveEvent(String monitorNo, String chainInfo) {
        super(UUID.randomUUID().toString());
        this.monitorNo = monitorNo;
        this.chainInfo = chainInfo;
    }
}
