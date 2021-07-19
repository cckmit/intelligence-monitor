package com.zhikuntech.intellimonitor.alarm.domain.service.impl.corelogic;

import com.zhikuntech.intellimonitor.alarm.domain.service.corelogic.CoreLogicStatusMaintainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 状态机制
 *
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoreLogicStatusMaintainServiceImpl implements CoreLogicStatusMaintainService, ApplicationListener<CoreLogicStatusMaintainServiceImpl.StatusDataReceiveEvent> {

    //# ------------------------------------------------状态监听处理------------------------------------------------

    @Override
    public void onApplicationEvent(StatusDataReceiveEvent event) {

    }

    public static class StatusDataReceiveEvent extends ApplicationEvent {

        private static final long serialVersionUID = -2705750725363172932L;

        /**
         * Create a new {@code ApplicationEvent}.
         *
         * @param source the object on which the event initially occurred or with
         *               which the event is associated (never {@code null})
         */
        public StatusDataReceiveEvent(Object source) {
            super(source);
        }
    }

    //# ------------------------------------------------状态监听处理------------------------------------------------



}
