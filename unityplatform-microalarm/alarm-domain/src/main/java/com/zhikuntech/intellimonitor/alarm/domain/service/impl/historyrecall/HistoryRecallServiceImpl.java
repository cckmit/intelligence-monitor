package com.zhikuntech.intellimonitor.alarm.domain.service.impl.historyrecall;

import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.alarm.domain.dto.historyrecall.HistoryRecallDTO;
import com.zhikuntech.intellimonitor.alarm.domain.query.historyrecall.HistoryRecallQuery;
import com.zhikuntech.intellimonitor.alarm.domain.service.historyrecall.HistoryRecallService;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/7/19 16:21
 * @Description 事故追忆
 * @Version 1.0
 */
@Service
@Slf4j
public class HistoryRecallServiceImpl implements HistoryRecallService {
    @Value("${golden.ip}")
    private String ip;
    @Value("${golden.port}")
    private Integer port;
    @Value("${golden.user}")
    private String user;
    @Value("${golden.password}")
    private String password;
    @Value("${golden.poolSize}")
    private Integer poolSize;
    @Value("${golden.maxSize}")
    private Integer maxSize;

    @Override
    public List<HistoryRecallDTO> getHistory(HistoryRecallQuery historyRecallQuery) {
        GoldenUtil.init(ip, port, user, password, poolSize, maxSize);
        try {
            List<RtdbData> archivedValues = GoldenUtil.getArchivedValues(historyRecallQuery.getId(),
                                                                        historyRecallQuery.getStartTime(),
                                                                        historyRecallQuery.getEndTime());
            if(null != archivedValues){
                log.info("长度->"+archivedValues.size());
            }
            List<HistoryRecallDTO> historyRecallDTOList = toHistoryRecallDTOList(archivedValues);
            return historyRecallDTOList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * List<RtdbData>转换成List<HistoryRecallDTO>
     * @param rtdbDataList
     * @return
     */
    public List<HistoryRecallDTO> toHistoryRecallDTOList(List<RtdbData> rtdbDataList){
        if(rtdbDataList == null){
            return null;
        }
        List<HistoryRecallDTO> historyRecallDTOList = new ArrayList<>();
        for(RtdbData rtdbData : rtdbDataList){
            HistoryRecallDTO historyRecallDTO = new HistoryRecallDTO();
            historyRecallDTO.setId(rtdbData.getId());
            historyRecallDTO.setDate(rtdbData.getDate());
            historyRecallDTO.setValue(new BigDecimal(rtdbData.getValue().toString()));
            historyRecallDTOList.add(historyRecallDTO);
        }
        return historyRecallDTOList;
    }
}
