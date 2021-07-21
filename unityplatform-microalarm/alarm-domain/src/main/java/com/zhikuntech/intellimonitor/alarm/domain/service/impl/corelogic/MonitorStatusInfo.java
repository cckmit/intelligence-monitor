package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * 测点状态信息
 *
 * @author liukai
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MonitorStatusInfo implements Serializable {

    private static final long serialVersionUID = -1354999979597391922L;

    public static final Integer SIGNAL_DATA_ALARM = 11;

    public static final Integer SIGNAL_DATA_NO_ALARM = 22;



    /**
     * 测点编码
     */
    private String monitorNo;

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
     * 状态是否发生了变化
     *
     * @return true -> 有变化 / false -> 无变化
     */
    public boolean statusChangeOrNot() {
        return statusChange == 1;
    }

    /**
     * 晚于当前时间的事件直接舍弃
     *
     * @param timeStamp 新数据的时间戳
     * @return  是否可以被处理
     */
    private boolean canJoinProcess(long timeStamp) {
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
        this.monitorPreStatus = this.monitorCurStatus;
        this.preTimeStamp = this.curTimeStamp;
        // 当前状态保存最新的状态
        this.monitorCurStatus = newestStatus;
        this.curTimeStamp = newestTimeStamp;

        // 刷新状态变更信息
        assignStatusChangeAndFlushStatus();
    }

}
