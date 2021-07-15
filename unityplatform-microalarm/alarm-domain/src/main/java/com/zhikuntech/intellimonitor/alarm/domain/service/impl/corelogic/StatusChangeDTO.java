package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liukai
 */
@Data
public class StatusChangeDTO implements Serializable {

    private static final long serialVersionUID = 2466245788607178014L;

    /**
     * 测点类型
     */
    private Integer monitorType;

    /**
     * 上一个状态
     */
    private Integer lastStatus;

    /**
     * 当前状态
     */
    private Integer currentStatus;

}
