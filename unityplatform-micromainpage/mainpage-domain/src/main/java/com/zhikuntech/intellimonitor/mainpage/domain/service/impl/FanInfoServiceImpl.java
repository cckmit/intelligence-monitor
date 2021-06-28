package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.SocketException;
import java.net.URL;
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

    @Value("${template.file.path}")
    private String filePath;

    @Override
    public List<FanRuntimeDTO> getRuntimeInfos() throws Exception {
        List<FanRuntimeDTO> list = new ArrayList<>();
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<ValueData> valueData = getSnapShops(ids);
        if (null == valueData) {
            return null;
        }
        for (int i = 1; i <= 63; i++) {
            FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
            fanRuntimeDto.setNumber(i);
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, i, valueData);
            list.add(dto);
        }
        for (FanRuntimeDTO dto : list) {
            Double obj = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_POWER + dto.getNumber());
            double powerGeneration = null == obj ? 0 : obj;
            dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration() - powerGeneration);
        }
        return list;
    }

    @Override
    public void getRuntimeInfos(String user) throws Exception {
        if (goldenUtil.getServer().containsKey(user)) {
            return;
        }
        if (WebSocketServer.GROUP_RUNTIME.keySet().size() > 0) {
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            List<FanRuntimeDTO> list = new ArrayList<>(10);
            for (int i = 1; i <= 63; i++) {
                FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
                fanRuntimeDto.setNumber(i);
                list.add(fanRuntimeDto);
            }
            try {
                goldenUtil.subscribeSnapshots(user, ids, (data) -> {
                    try {
                        if (WebSocketServer.GROUP_RUNTIME.keySet().size() > 0) {
                            long l0 = System.currentTimeMillis();
                            List<FanRuntimeDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data);
                            if (null != dtos) {
                                for (FanRuntimeDTO dto : dtos) {
                                    Double obj = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_POWER + dto.getNumber());
                                    double powerGeneration = null == obj ? 0 : obj;
                                    dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration() - powerGeneration);
                                }
                                String jsonString = JSONObject.toJSONString(dtos);
                                jsonString = WebSocketConstant.MAIN_PAGE_RUNTIME + WebSocketConstant.PATTERN + jsonString;
                                try {
                                    webSocketServer.sendGroupMessage(jsonString, 0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            long l1 = System.currentTimeMillis();
                            log.info("实时数据，处理时间{}，消息时间{}", l1 - l0, data[0].getDate());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        goldenUtil.cancel(user);
                        log.info("回调函数内部触发所有取消操作");
                    }
                });
            } catch (SocketException e) {
                log.info("golden连接失败，重连后取消之前所有连接");
                goldenUtil.cancelAll();
                e.printStackTrace();
                webSocketServer.sendAllMessage("重新订阅");
                log.info("faninfoservice触发所有取消操作");
            }
        }
    }

    @Override
    public FanStatisticsDTO getStatistics() throws Exception {
        int[] ids = {1, 2, 13, 14};
        FanStatisticsDTO dto = new FanStatisticsDTO();
        List<ValueData> valueData = getSnapShops(ids);
        if (null == valueData) {
            return null;
        }
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
        if (WebSocketServer.GROUP_STATISTICS.keySet().size() > 0) {
            int[] ids = {1, 2, 13, 14};
            List<FanStatisticsDTO> list = new ArrayList<>();
            for (int i = 1; i <= 63; i++) {
                list.add(new FanStatisticsDTO());
            }
            try {
                goldenUtil.subscribeSnapshots(user, ids, (data) -> {
                    try {
                        if (WebSocketServer.GROUP_STATISTICS.keySet().size() > 0) {
                            long l0 = System.currentTimeMillis();
                            List<FanStatisticsDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data);
                            if (null != dtos) {
                                FanStatisticsDTO dto = injecctPorerties(dtos);
                                String jsonString = JSONObject.toJSONString(dto);
                                jsonString = WebSocketConstant.MAIN_PAGE_STATISTICS + WebSocketConstant.PATTERN + jsonString;
                                try {
                                    webSocketServer.sendGroupMessage(jsonString, 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            long l1 = System.currentTimeMillis();
                            log.info("统计数据，处理时间{}，消息时间{}", l1 - l0, data[0].getDate());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        goldenUtil.cancel(user);
                        log.info("回调函数内部触发所有取消操作");
                    }
                });
            } catch (SocketException e) {
                log.info("golden连接失败，重连后取消之前所有连接");
                e.printStackTrace();
                goldenUtil.cancelAll();
                log.info("faninfoservice触发所有取消操作");
            }
        }
    }

    @Override
    public void export(HttpServletResponse response) throws Exception {
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<FanRuntimeDTO> list = new ArrayList<>();
        List<ValueData> valueData = getSnapShops(ids);
        if (null == valueData) {
            return;
        }
        URL url = new File(filePath + "1.png").toURL();
        for (int i = 1; i <= 63; i++) {
            FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
            fanRuntimeDto.setNumber(i);
            fanRuntimeDto.setRunningStatusUrl(url);
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, i, valueData);
            list.add(dto);
        }
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("风机列表", "UTF-8") + ".xlsx");
        EasyExcelUtil.export(response.getOutputStream(), "风机列表", list);
    }

    /**
     * 处理golden socket连接异常后原有连接失效问题
     */
    private List<ValueData> getSnapShops(int[] ids) throws Exception {
        List<ValueData> valueData;
        try {
            valueData = goldenUtil.getSnapshots(ids);
        } catch (SocketException e) {
            log.info("golden连接失败，重连后取消之前所有连接");
            goldenUtil.cancelAll();
            webSocketServer.sendAllMessage("重新订阅");
            return null;
        }
        return valueData;
    }

    private FanStatisticsDTO injecctPorerties(List<FanStatisticsDTO> dtos) {
        Double obj1 = FanInfoInit.POWER_MAP.get(FanConstant.DAILY_POWER_ALL);
        double dailyPowerGeneration = null == obj1 ? 0 : obj1;
        Double obj2 = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_POWER_ALL);
        double monthlyPowerGeneration = null == obj2 ? 0 : obj2;
        Double obj3 = FanInfoInit.POWER_MAP.get(FanConstant.ANNUAL_POWER_ALL);
        double annualPowerGeneration = null == obj3 ? 0 : obj3;
        Double obj4 = FanInfoInit.POWER_MAP.get(FanConstant.DAILY_ONLINE_ALL);
        double dailyOnlinePower = null == obj4 ? 0 : obj4;
        Double obj5 = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_ONLINE_ALL);
        double monthlyOnlinePower = null == obj5 ? 0 : obj5;
        Double obj6 = FanInfoInit.POWER_MAP.get(FanConstant.ANNUAL_ONLINE_ALL);
        double annualOnlinePower = null == obj6 ? 0 : obj6;

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
