package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDto;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDto;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.RedisUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/8 10:27
 */
@Service
@Slf4j
public class FanInfoServiceImpl implements FanInfoService {

    @Autowired
    private GoldenUtil goldenUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<FanRuntimeDto> getRuntimeInfos() throws Exception {
        List<FanRuntimeDto> list = new ArrayList<>();
//        int[] ids = goldenUtil.getIds("fan");
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        for (int i = 1; i <= 10; i++) {
            FanRuntimeDto fanRuntimeDto = new FanRuntimeDto();
            fanRuntimeDto.setNumber(i);
            FanRuntimeDto dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, valueData);
            list.add(dto);
        }
        return InjectPropertiesUtil.injectByAnnotation(list, valueData);
    }

    @Override
    public void getRuntimeInfos(String user) throws Exception {
        if (webSocketServer.getClients().containsKey(user)) {
//            int[] ids = goldenUtil.getIds("fan");
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            List<FanRuntimeDto> list = new ArrayList<>(10);
            for (int i = 1; i <= 10; i++) {
                FanRuntimeDto fanRuntimeDto = new FanRuntimeDto();
                fanRuntimeDto.setNumber(i);
                list.add(fanRuntimeDto);
            }
            goldenUtil.subscribeSnapshots(user, ids, (data) -> {
                if (!webSocketServer.getClients().containsKey(user)) {
                   return;
                } else {
                    List<FanRuntimeDto> dtos = InjectPropertiesUtil.injectByAnnotation(list, data);
                    if (null != dtos) {
                        String jsonString = JSONObject.toJSONString(dtos);
                        webSocketServer.sendMessage(jsonString, user);
                    }
                }
            });
        }
    }

    @Override
    public FanStatisticsDto getStatistics() throws Exception {
        int[] ids = {13, 14};
        FanStatisticsDto fanStatisticsDto = new FanStatisticsDto();
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        FanStatisticsDto dto = InjectPropertiesUtil.injectByAnnotation(fanStatisticsDto, valueData);
//        if(null==dto){
//            return null;
//        }
//        dto.setNum(63);
//        dto.setActivePower(dto.getEnergyOutput());
//        dto.set
        return dto;
    }

    @Override
    public void getStatistics(String user) throws Exception {

    }
}
