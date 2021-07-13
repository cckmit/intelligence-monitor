package com.zhikuntech.intellimonitor.alarm.domain.service.impl.external;

import com.zhikuntech.intellimonitor.alarm.domain.dto.external.CurrentStatusByMonitorDTO;
import com.zhikuntech.intellimonitor.alarm.domain.dto.external.FetchAlarmNumWithGroupDTO;
import com.zhikuntech.intellimonitor.alarm.domain.service.external.ProvideToExternalAlarmInfoService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

            return mocks;
        }

        return null;
    }

    @Override
    public List<CurrentStatusByMonitorDTO> fetchMonitorCurrentStatus(List<String> monitorIds) {
        // todo

        if (true) {
            ArrayList<CurrentStatusByMonitorDTO> mocks = new ArrayList<>();

            return mocks;
        }

        return null;
    }

}
