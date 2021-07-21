package com.zhikuntech.intellimonitor.core.stream;

import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.core.prototype.MonitorStructDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 代志豪
 * 2021/7/8 11:03
 */
public class DataConvertUtils {

    public static void convertAndSend(RtdbData[] data) {
        List<MonitorStructDTO> dtos = new ArrayList<>();
        MonitorStructDTO dto = new MonitorStructDTO();
        String value;
        dto.setEventTimeStamp(data[0].getDate().getTime());
        for (RtdbData rtdbData : data) {
            dto.setUuid(UUID.randomUUID().toString());
            dto.setMonitorNo(String.valueOf(rtdbData.getId()));
            value = rtdbData.getValue().toString();
            dto.setMonitorValue(new BigDecimal(value));
            dto.setMonitorValueStr(value);
            dtos.add(dto);
        }
        DataProduceUtils.sendData(dtos);
    }

}
