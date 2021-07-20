package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicAlarmRelieveService;
import com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic.event.AlarmRelieveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoreLogicAlarmRelieveServiceImpl implements CoreLogicAlarmRelieveService, ApplicationListener<AlarmRelieveEvent> {

    //# ------------------------------------------------告警恢复处理------------------------------------------------

    @Override
    public void onApplicationEvent(AlarmRelieveEvent event) {

    }

    //# ------------------------------------------------告警恢复处理------------------------------------------------

}
