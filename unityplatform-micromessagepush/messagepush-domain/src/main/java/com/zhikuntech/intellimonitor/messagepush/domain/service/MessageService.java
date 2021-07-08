package com.zhikuntech.intellimonitor.messagepush.domain.service;

/**
 * @author 滕楠
 * @className MessageService
 * @create 2021/7/8 11:39
 **/
public interface MessageService {
    /**
     * 海缆监测告警信息
     * @param connectName golden连接名
     */
    void cableAlarmMessagePush(String connectName);
    /**
     * 在线监测告警信息
     * @param connectName golden连接名
     */
    void onlineAlarmMessagePush(String connectName);
    /**
     * 结构监测告警信息
     * @param connectName golden连接名
     */
    void structureAlarmMessagePush(String connectName);
    /**
     * 告警列表信息
     * @param connectName golden连接名
     */
    void alarmListMessagePush(String connectName);
}