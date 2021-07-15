package com.zhikuntech.intellimonitor.alarm.domain.service.impl.external;

import com.zhikuntech.intellimonitor.alarm.domain.dto.external.CurrentStatusByMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.external.FetchAlarmNumWithGroupDTO;
import com.zhikuntech.intellimonitor.alarm.domain.service.external.ProvideToExternalAlarmInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProvideToExternalAlarmInfoServiceImpl implements ProvideToExternalAlarmInfoService {


    @Override
    public List<FetchAlarmNumWithGroupDTO> fetchWithGroup(List<String> groupNos) {
        // todo

        if (true) {
            // mock data
            ArrayList<FetchAlarmNumWithGroupDTO> mocks = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(groupNos)) {
                for (String groupNo : groupNos) {
                    FetchAlarmNumWithGroupDTO tmp = FetchAlarmNumWithGroupDTO
                            .builder()
                            .groupName(groupNo)
                            .count(ThreadLocalRandom.current().nextInt(0, 20))
                            .build();
                    mocks.add(tmp);
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    FetchAlarmNumWithGroupDTO tmp = FetchAlarmNumWithGroupDTO
                            .builder()
                            .groupName("group" + i)
                            .count(ThreadLocalRandom.current().nextInt(0, 20))
                            .build();
                    mocks.add(tmp);
                }
            }
            return mocks;
        }

        return null;
    }

    @Override
    public List<CurrentStatusByMonitorDTO> fetchMonitorCurrentStatus(List<String> monitorIds) {
        // todo

        if (true) {
            ArrayList<CurrentStatusByMonitorDTO> mocks = new ArrayList<>();
            if (CollectionUtils.isEmpty(monitorIds)) {
                return mocks;
            }

            for (String monitorId : monitorIds) {
                boolean b = ThreadLocalRandom.current().nextBoolean();

                CurrentStatusByMonitorDTO monitorDTO = CurrentStatusByMonitorDTO.builder()
                        .monitorId(monitorId)
                        .monitorName(monitorId)
                        .isFlash(ThreadLocalRandom.current().nextBoolean())
                        .textColor("rgb(255,255,255)")
                        .isAlarmStatus(b)
                        .alarmTime(b ? System.currentTimeMillis() : null)
                        .build();
                // yyyy-MM-dd HH:mm:ss
                mocks.add(monitorDTO);
            }

            return mocks;
        }

        return null;
    }

}
