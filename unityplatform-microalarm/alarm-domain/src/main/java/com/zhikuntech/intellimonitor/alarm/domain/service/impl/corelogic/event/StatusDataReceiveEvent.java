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

    /**
     * 测点类型
     */
    @Getter
    private final Integer monitorType;

    /**
     * 事件时间戳
     */
    @Getter
    private final Long eventTimeStamp;

    /**
     * 测点状态
     */
    @Getter
    private final Integer newestStatus;

    public StatusDataReceiveEvent(String monitorNo, Integer monitorType, Long eventTimeStamp, Integer newestStatus) {
        super(UUID.randomUUID().toString());
        this.monitorNo = monitorNo;
        this.monitorType = monitorType;
        this.eventTimeStamp = eventTimeStamp;
        this.newestStatus = newestStatus;
    }

}
