package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * @author liukai
 */
@ToString
public class StatusDataReceiveEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2705750725363172932L;

    /**
     * 测点编码
     */
    @Getter
    private final String monitorNo;

    public StatusDataReceiveEvent(String monitorNo) {
        super(UUID.randomUUID().toString());
        this.monitorNo = monitorNo;
    }

}
