package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicStatusMaintainService;
import com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event.StatusDataReceiveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 状态机制
 *
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoreLogicStatusMaintainServiceImpl implements CoreLogicStatusMaintainService, ApplicationListener<StatusDataReceiveEvent> {


    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;


    //# ------------------------------------------------状态监听处理------------------------------------------------

    @Override
    public void onApplicationEvent(StatusDataReceiveEvent event) {
        final String monitorNo = event.getMonitorNo();
        final Integer monitorType = event.getMonitorType();
        final Long eventTimeStamp = event.getEventTimeStamp();
        final Integer newestStatus = event.getNewestStatus();
        if (StringUtils.isBlank(monitorNo) ||
                Objects.isNull(monitorType) ||
                Objects.isNull(eventTimeStamp) ||
                Objects.isNull(newestStatus)) {
            log.error("(测点编码/测点类型/事件时间戳/最新状态)均不能为空, 状态事件无法处理:[{}]", event);
            return;
        }
        MonitorStatusInfo monitorStatusInfo = obtainAlarmStatusByMonitorId(monitorNo);
        if /* 不存在当前状态 */ (Objects.isNull(monitorStatusInfo)) {
            // 创建状态信息并保存 -> 首次数据进来没有状态变化
            final MonitorStatusInfo statusInfo = MonitorStatusInfo.builder()
                    .monitorNo(monitorNo)
                    .monitorType(monitorType)
                    .curTimeStamp(eventTimeStamp)
                    .monitorCurStatus(newestStatus)
                    .build();
            try {
                String statusSaveStr = objectMapper.writeValueAsString(statusInfo);
                redisTemplate.opsForHash().put(RedisCacheKeyNameConstants.REDIS_STATUS, monitorNo, statusSaveStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } /* 存在当前状态 */ else {
            // 1.交换状态信息(将当前状态信息设置为上次状态，并将接收到的状态信息设置为当前状态)
            monitorStatusInfo.swapCurStatusAndPre(newestStatus, eventTimeStamp);
            // 2.存储该状态信息
            try {
                String statusSaveStr = objectMapper.writeValueAsString(monitorStatusInfo);
                redisTemplate.opsForHash().put(RedisCacheKeyNameConstants.REDIS_STATUS, monitorNo, statusSaveStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // 3.判断状态是否发生变化
            final boolean statusChange = monitorStatusInfo.statusChangeOrNot();
            if /* 状态发生变化 */ (statusChange) {
                // todo
                log.debug("[{}]状态发生变化", event);
            } /* 状态没有发生变化 */ else {
                // todo
                log.debug("[{}]状态没有发生变化", event);
            }
        }
    }

    //# ------------------------------------------------状态监听处理------------------------------------------------


    /**
     * 获取告警状态
     * @param monitorId     测点id
     * @return  告警状态
     */
    public MonitorStatusInfo obtainAlarmStatusByMonitorId(String monitorId) {
        String cacheEntry = (String) redisTemplate.opsForHash().get(RedisCacheKeyNameConstants.REDIS_STATUS, monitorId);
        if (Objects.isNull(cacheEntry)) {
            return null;
        }
        MonitorStatusInfo monitorStatusInfo = null;
        try {
            monitorStatusInfo = objectMapper.readValue(cacheEntry, MonitorStatusInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return monitorStatusInfo;
    }

}
