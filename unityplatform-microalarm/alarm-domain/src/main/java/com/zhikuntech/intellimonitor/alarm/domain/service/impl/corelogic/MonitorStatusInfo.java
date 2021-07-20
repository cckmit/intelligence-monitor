package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 测点状态信息
 *
 * @author liukai
 */
@Data
public class MonitorStatusInfo implements Serializable {

    private static final long serialVersionUID = -1354999979597391922L;

    /**
     * 测点id
     */
    private String monitorId;

    /**
     * 测点类型
     * 0 遥信数据
     * 1 遥测数据
     */
    private Integer monitorType;

    //# ------------------------测点当前状态------------------------

    /**
     * 测点当前状态
     * ----(遥测数据)
     * 0 -> 无告警
     * 1 -> 一级预警
     * 2 -> 二级预警
     * 100 -> 告警
     *
     * ----(遥信数据)
     * 11   告警状态
     * 22   无告警状态
     */
    private Integer monitorCurStatus;

    /**
     * 当前状态时间戳
     */
    private Long curTimeStamp;

    //# ------------------------测点上一个状态------------------------

    /**
     * 测点上一个状态
     */
    private Integer monitorPreStatus;

    /**
     * 上一个状态的时间戳
     */
    private Long preTimeStamp;


    //# ------------------------其他信息------------------------

    /**
     * 状态是否变更 (0无1有)
     */
    private Integer statusChange;

    /**
     * 晚于当前时间的事件直接舍弃
     *
     * @param timeStamp 新数据的时间戳
     * @return  是否可以被处理
     */
    public boolean canJoinProcess(long timeStamp) {
        return curTimeStamp < timeStamp;
    }

    public void assignStatusChangeAndFlushStatus() {
        Integer tmpStatus = statusChange;
        statusChange = Objects.equals(monitorCurStatus, monitorPreStatus) ? 0 : 1;
        if (!Objects.equals(tmpStatus, statusChange)) {
            // todo 刷新状态信息

        }
    }

    public void swapCurStatusAndPre(Integer newestStatus, Long newestTimeStamp) {
        // 是否可以被处理
        if (!canJoinProcess(newestTimeStamp)) {
            return;
        }
        // 上一个状态保存为当前状态
        monitorPreStatus = monitorCurStatus;
        preTimeStamp = curTimeStamp;
        // 当前状态保存最新的状态
        monitorCurStatus = newestStatus;
        curTimeStamp = newestTimeStamp;

        // 刷新状态变更信息
        assignStatusChangeAndFlushStatus();
    }

}
