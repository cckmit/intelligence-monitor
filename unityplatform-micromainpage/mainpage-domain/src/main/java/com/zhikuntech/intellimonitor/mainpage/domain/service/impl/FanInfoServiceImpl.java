package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.schedule.FanInfoInit;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.EasyExcelUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.RedisUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
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

    @Autowired
    private FanInfoInit fanInfoInit;

    @Override
    public List<FanRuntimeDTO> getRuntimeInfos() throws Exception {
        List<FanRuntimeDTO> list = new ArrayList<>();
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        for (int i = 1; i <= 63; i++) {
            FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
            fanRuntimeDto.setNumber(i);
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, i, valueData);
            list.add(dto);
        }
        for (FanRuntimeDTO dto : list) {
            Object obj = redisUtil.get(FanConstant.MONTHLY_POWER + dto.getNumber());
            double powerGeneration = null == obj ? 0 : (double) obj;
            dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration() - powerGeneration);
        }
        return list;
    }

    @Override
    public void getRuntimeInfos(String user) throws Exception {
        if (goldenUtil.getServer().containsKey(user)) {
            return;
        }
        if (WebSocketServer.clients.containsKey(user)) {
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            List<FanRuntimeDTO> list = new ArrayList<>(10);
            for (int i = 1; i <= 10; i++) {
                FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
                fanRuntimeDto.setNumber(i);
                list.add(fanRuntimeDto);
            }
            goldenUtil.subscribeSnapshots(user, ids, (data) -> {
                if (!WebSocketServer.clients.containsKey(user)) {
                    return;
                } else {
                    long l0 = System.currentTimeMillis();
                    List<FanRuntimeDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data);
                    if (null != dtos) {
                        for (FanRuntimeDTO dto : dtos) {
                            double powerGeneration = 0;
                            Object obj = redisUtil.get(FanConstant.MONTHLY_POWER + dto.getNumber());
//                            powerGeneration = null == obj ? 0 : (double) obj;
                            if (null == obj) {
                                int id = (int) redisUtil.get(FanConstant.GOLDEN_ID_POWER + dto.getNumber());
                                powerGeneration = fanInfoInit.dataResetFloat(FanConstant.MONTHLY_POWER + dto.getNumber(), id, 0);
                            } else {
                                powerGeneration = (double) obj;
                            }
                            dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration() - powerGeneration);
                        }
                        String jsonString = JSONObject.toJSONString(dtos);
                        webSocketServer.sendMessage(jsonString, user);
                    }
                    long l1 = System.currentTimeMillis();
                    log.info("实时数据，当前用户{}，处理时间{}，消息时间{}", user, l1 - l0, data[0].getDate());
                }
            });
        }
    }

    @Override
    public FanStatisticsDTO getStatistics() throws Exception {
        int[] ids = {1, 2, 13, 14};
        FanStatisticsDTO dto = new FanStatisticsDTO();
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        List<FanStatisticsDTO> list = new ArrayList<>();
        for (int i = 1; i <= 63; i++) {
            list.add(new FanStatisticsDTO());
        }
        List<FanStatisticsDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, valueData);
        if (null != dtos) {
            dto = injecctPorerties(dtos);
        }
        return dto;
    }

    @Override
    public void getStatistics(String user) throws Exception {
        if (goldenUtil.getServer().containsKey(user)) {
            return;
        }
        if (WebSocketServer.clients.containsKey(user)) {
            int[] ids = {1, 2, 13, 14};
            List<FanStatisticsDTO> list = new ArrayList<>();
            for (int i = 1; i <= 63; i++) {
                list.add(new FanStatisticsDTO());
            }
            goldenUtil.subscribeSnapshots(user, ids, (data) -> {
                if (!WebSocketServer.clients.containsKey(user)) {
                    return;
                } else {
                    long l0 = System.currentTimeMillis();
                    List<FanStatisticsDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data);
                    if (null != dtos) {
                        FanStatisticsDTO dto = injecctPorerties(dtos);
                        String jsonString = JSONObject.toJSONString(dto);
                        webSocketServer.sendMessage(jsonString, user);
                    }
                    long l1 = System.currentTimeMillis();
                    log.info("统计数据，当前用户{}，处理时间{}，消息时间{}", user, l1 - l0, data[0].getDate());
                }
            });
        }
    }

    @Override
    public void export(HttpServletResponse response) throws Exception {
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<FanRuntimeDTO> list = new ArrayList<>();
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        for (int i = 1; i <= 10; i++) {
            FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
            fanRuntimeDto.setNumber(i);
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, i, valueData);
            list.add(dto);
        }
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("风机列表", "UTF-8") + ".xlsx");
        EasyExcelUtil.export(response.getOutputStream(), "风机列表", list);
    }

    private FanStatisticsDTO injecctPorerties(List<FanStatisticsDTO> dtos) {
        Object obj1 = redisUtil.get(FanConstant.DAILY_POWER_ALL);
        double dailyPowerGeneration = null == obj1 ? 0 : (double) obj1;
        Object obj2 = redisUtil.get(FanConstant.MONTHLY_POWER_ALL);
        double monthlyPowerGeneration = null == obj2 ? 0 : (double) obj2;
        Object obj3 = redisUtil.get(FanConstant.ANNUAL_POWER_ALL);
        double annualPowerGeneration = null == obj3 ? 0 : (double) obj3;
        Object obj4 = redisUtil.get(FanConstant.DAILY_ONLINE_ALL);
        double dailyOnlinePower = null == obj4 ? 0 : (double) obj4;
        Object obj5 = redisUtil.get(FanConstant.MONTHLY_ONLINE_ALL);
        double monthlyOnlinePower = null == obj5 ? 0 : (double) obj5;
        Object obj6 = redisUtil.get(FanConstant.ANNUAL_ONLINE_ALL);
        double annualOnlinePower = null == obj6 ? 0 : (double) obj6;

        double activePower = dtos.stream().parallel().mapToDouble(FanStatisticsDTO::getActivePower).sum();
        double averageWindVelocity = dtos.stream().parallel().filter(e -> null != e.getAverageWindVelocity())
                .mapToDouble(FanStatisticsDTO::getAverageWindVelocity).average().getAsDouble();
        double energyOutput = dtos.stream().parallel().mapToDouble(FanStatisticsDTO::getEnergyOutput).sum();
        double reverseActivePower = dtos.stream().parallel().mapToDouble(FanStatisticsDTO::getReverseActivePower).sum();
        FanStatisticsDTO dto = new FanStatisticsDTO();
        dto.setNum(63);
        dto.setCapacity(63 * 4d);
        dto.setActivePower(activePower);
        dto.setAverageWindVelocity(averageWindVelocity);
        dto.setDailyOnlinePower(reverseActivePower - dailyOnlinePower);
        dto.setMonthlyOnlinePower(reverseActivePower - monthlyOnlinePower);
        dto.setAnnualOnlinePower(reverseActivePower - annualOnlinePower);
        dto.setDailyPowerGeneration(energyOutput - dailyPowerGeneration);
        dto.setMonthlyPowerGeneration(energyOutput - monthlyPowerGeneration);
        dto.setAnnualPowerGeneration(energyOutput - annualPowerGeneration);
        return dto;
    }
}
