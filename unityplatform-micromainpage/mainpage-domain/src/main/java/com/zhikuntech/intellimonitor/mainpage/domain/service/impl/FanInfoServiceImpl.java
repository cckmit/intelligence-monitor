package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rtdb.api.model.RtdbData;
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
import org.springframework.data.redis.core.RedisTemplate;
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
        int[] ids = goldenUtil.getIds("fan");
        FanRuntimeDto fanRuntimeDto = new FanRuntimeDto();
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        FanRuntimeDto runtimeDto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, valueData);
        list.add(runtimeDto);
        return list;
    }

    @Override
    public void getRuntimeInfos(String user) throws Exception {
        if (webSocketServer.getClients().containsKey(user)) {
            int[] ids = goldenUtil.getIds("fan");
            FanRuntimeDto fanRuntimeDto = new FanRuntimeDto();
            goldenUtil.subscribeSnapshots(ids, (data) -> {
                if (!webSocketServer.getClients().containsKey(user)) {
                    try {
                        goldenUtil.cancel();
                    } catch (Exception e) {
                        log.info("websocket用户{}连接断开,庚顿订阅取消！", user);
                    }
                }
                FanRuntimeDto runtimeDto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, data);
                if (null != runtimeDto) {
                    String jsonString = JSONObject.toJSONString(runtimeDto);
                    webSocketServer.sendMessage(jsonString, user);
                }
            });
        }
    }

    @Override
    public FanStatisticsDto getStatistics() throws Exception {
        int[] ids = goldenUtil.getIds("fan");
        FanStatisticsDto fanStatisticsDto = new FanStatisticsDto();
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        FanStatisticsDto dto = InjectPropertiesUtil.injectByAnnotation(fanStatisticsDto, valueData);
//        Object o = redisUtil.get();
        return dto;
    }

    @Override
    public void getStatistics(String user) throws Exception {

    }
}
