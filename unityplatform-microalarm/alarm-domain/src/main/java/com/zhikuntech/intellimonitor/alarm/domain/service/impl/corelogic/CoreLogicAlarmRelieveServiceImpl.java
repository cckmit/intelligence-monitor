package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmProduceInfo;
import com.zhikuntech.intellimonitor.alarm.domain.service.IAlarmProduceInfoService;
import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicAlarmRelieveService;
import com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event.AlarmRelieveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoreLogicAlarmRelieveServiceImpl implements CoreLogicAlarmRelieveService, ApplicationListener<AlarmRelieveEvent> {


    private final IAlarmProduceInfoService infoService;


    //# ------------------------------------------------告警恢复处理------------------------------------------------

    @Override
    public void onApplicationEvent(AlarmRelieveEvent event) {
        String monitorNo = event.getMonitorNo();
        String chainInfo = event.getChainInfo();
        if (StringUtils.isBlank(chainInfo)) {
            log.error("告警链编码为空,测点编码为:[{}]", monitorNo);
            return;
        }

        // 恢复当前告警及所关联的历史告警
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        UpdateWrapper<AlarmProduceInfo> produceInfoUpdateWrapper = new UpdateWrapper<>();
        produceInfoUpdateWrapper.eq("chain_info", chainInfo);
        produceInfoUpdateWrapper.setSql(String.format("has_restore=%d,restore_time='%s'", 1, nowStr));
        infoService.getBaseMapper().update(null, produceInfoUpdateWrapper);
        // 需要将当前告警改变为历史告警(待确认)
        UpdateWrapper<AlarmProduceInfo> updateCurAlarmWrapper = new UpdateWrapper<>();
        updateCurAlarmWrapper.eq("chain_info", chainInfo);
        updateCurAlarmWrapper.isNull("next_info_no");
        updateCurAlarmWrapper.setSql("with_history=1");
        infoService.getBaseMapper().update(null, updateCurAlarmWrapper);
    }

    //# ------------------------------------------------告警恢复处理------------------------------------------------

}
