package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.golden.InjectPropertiesUtil;
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

    @Override
    public List<FanRuntimeDTO> getRuntimeInfos() throws Exception {
        List<FanRuntimeDTO> list = new ArrayList<>();
//        int[] ids = goldenUtil.getIds("fan");
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        for (int i = 1; i <= 10; i++) {
            FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
            fanRuntimeDto.setNumber(i);
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, valueData);
            list.add(dto);
        }
        for (FanRuntimeDTO dto : list) {
            Object obj = redisUtil.get("powerGeneration_day_"+dto.getNumber());
            double powerGeneration = null == obj ? 0 : (double) obj;
            dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration() - powerGeneration);
        }
        return InjectPropertiesUtil.injectByAnnotation(list, valueData);
    }

    @Override
    public void getRuntimeInfos(String user) throws Exception {
        if(goldenUtil.getServer().containsKey(user)){
            return;
        }
        if (WebSocketServer.clients.containsKey(user)) {
//            int[] ids = goldenUtil.getIds("fan");
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            List<FanRuntimeDTO> list = new ArrayList<>(10);
            goldenUtil.subscribeSnapshots(user, ids, (data) -> {
                if (!WebSocketServer.clients.containsKey(user)) {
                    return;
                } else {
                    long l0 = System.currentTimeMillis();
                    for (int i = 1; i <= 10; i++) {
                        FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
                        fanRuntimeDto.setNumber(i);
                        list.add(fanRuntimeDto);
                    }
                    List<FanRuntimeDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data);
                    if (null != dtos) {
                        Object obj = redisUtil.get("powerGeneration_month");
                        double powerGeneration = null == obj ? 0 : (double) obj;
                        for (FanRuntimeDTO dto : dtos) {
                            dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration() - powerGeneration);
                        }
                        String jsonString = JSONObject.toJSONString(dtos);
                        webSocketServer.sendMessage(jsonString, user);
                    }
                    long l1 = System.currentTimeMillis();
                    log.info("实时数据，当前用户{}，处理时间{}，消息时间{}",user,l1-l0,data[0].getDate());
                }
            });
        }
    }

    @Override
    public FanStatisticsDTO getStatistics() throws Exception {
        int[] ids = {1, 2, 13, 14};
        FanStatisticsDTO fanStatisticsDto = new FanStatisticsDTO();
        List<ValueData> valueData = goldenUtil.getSnapshots(ids);
        FanStatisticsDTO dto = InjectPropertiesUtil.injectByAnnotation(fanStatisticsDto, valueData);
        if (null != dto) {
            dto.setNum(63);
            dto.setCapacity(63 * 4d);
            dto.setActivePower(63 * dto.getActivePower());
            dto.setDailyOnlinePower(dto.getReverseActivePower());
            dto.setMonthlyOnlinePower(dto.getReverseActivePower());
            dto.setAnnualOnlinePower(dto.getReverseActivePower());
            dto.setDailyPowerGeneration(dto.getEnergyOutput());
            dto.setMonthlyPowerGeneration(dto.getEnergyOutput());
            dto.setAnnualPowerGeneration(dto.getEnergyOutput());
        }
        return dto;
    }

    @Override
    public void getStatistics(String user) throws Exception {
        if(goldenUtil.getServer().containsKey(user)){
            return;
        }
        if (WebSocketServer.clients.containsKey(user)) {
            int[] ids = {1, 2, 13, 14};
            goldenUtil.subscribeSnapshots(user, ids, (data) -> {
                if (!WebSocketServer.clients.containsKey(user)) {
                    return;
                } else {
                    long l0 = System.currentTimeMillis();
                    FanStatisticsDTO dto = InjectPropertiesUtil.injectByAnnotation(new FanStatisticsDTO(), data);
                    if (null != dto) {
                        dto.setNum(63);
                        dto.setCapacity(63 * 4d);
                        dto.setActivePower(63 * dto.getActivePower());
                        dto.setDailyOnlinePower(dto.getReverseActivePower());
                        dto.setMonthlyOnlinePower(dto.getReverseActivePower());
                        dto.setAnnualOnlinePower(dto.getReverseActivePower());
                        dto.setDailyPowerGeneration(dto.getEnergyOutput());
                        dto.setMonthlyPowerGeneration(dto.getEnergyOutput());
                        dto.setAnnualPowerGeneration(dto.getEnergyOutput());
                        String jsonString = JSONObject.toJSONString(dto);
                        webSocketServer.sendMessage(jsonString, user);
                    }
                    long l1 = System.currentTimeMillis();
                    log.info("统计数据，当前用户{}，处理时间{}，消息时间{}",user,l1-l0,data[0].getDate());
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
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, valueData);
            list.add(dto);
        }
        response.setHeader("Access-Control-Expose-Headers" ,"*");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("风机列表", "UTF-8") + ".xlsx");
        EasyExcelUtil.export(response.getOutputStream(),"风机列表",list);
    }

    private void injecctPorerties(List<FanStatisticsDTO> dtos) {
        Object obj1 = redisUtil.get("powerGeneration_day");
        double dailyPowerGeneration = null == obj1 ? 0 : (double) obj1;
        Object obj2 = redisUtil.get("powerGeneration_month");
        double monthlyPowerGeneration = null == obj2 ? 0 : (double) obj2;
        Object obj3 = redisUtil.get("powerGeneration_year");
        double annualPowerGeneration = null == obj3 ? 0 : (double) obj3;
        Object obj4 = redisUtil.get("onLinePower_day");
        double dailyOnlinePower = null == obj4 ? 0 : (double) obj4;
        Object obj5 = redisUtil.get("onLinePower_month");
        double monthlyOnlinePower = null == obj5 ? 0 : (double) obj5;
        Object obj6 = redisUtil.get("onLinePower_year");
        double annualOnlinePower = null == obj6 ? 0 : (double) obj6;
        dtos.stream().parallel().forEach(e->{
            e.setDailyOnlinePower(e.getReverseActivePower() - dailyOnlinePower);
            e.setMonthlyOnlinePower(e.getReverseActivePower() - monthlyOnlinePower);
            e.setAnnualOnlinePower(e.getReverseActivePower() - annualOnlinePower);
            e.setDailyPowerGeneration(e.getEnergyOutput() - dailyPowerGeneration);
            e.setMonthlyPowerGeneration(e.getEnergyOutput() - monthlyPowerGeneration);
            e.setAnnualPowerGeneration(e.getEnergyOutput() - annualPowerGeneration);
        });
//        dtos.stream().parallel().map(dto->{
//            dto.
//        })
    }
}
