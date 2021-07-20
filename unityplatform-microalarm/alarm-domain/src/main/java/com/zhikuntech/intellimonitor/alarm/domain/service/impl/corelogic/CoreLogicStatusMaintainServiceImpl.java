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
        String monitorNo = event.getMonitorNo();
        if (StringUtils.isBlank(monitorNo)) {
            log.error("测点编码为空, 状态事件无法处理:[{}]", event);
            return;
        }
        MonitorStatusInfo monitorStatusInfo = obtainAlarmStatusByMonitorId(monitorNo);

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
