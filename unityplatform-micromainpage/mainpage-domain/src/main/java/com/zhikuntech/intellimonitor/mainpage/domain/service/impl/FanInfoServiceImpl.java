package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.core.commons.constant.WebSocketConstant;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanStatisticsDTO;
import com.zhikuntech.intellimonitor.mainpage.domain.schedule.FanInfoInit;
import com.zhikuntech.intellimonitor.core.commons.golden.TimerUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.service.FanInfoService;
import com.zhikuntech.intellimonitor.mainpage.domain.utils.EasyExcelUtil;
import com.zhikuntech.intellimonitor.mainpage.domain.websocket.MyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * @author 代志豪
 * 2021/6/8 10:27
 */
@Service
@Slf4j
public class FanInfoServiceImpl implements FanInfoService {

    @Autowired
    private MyWebSocketHandler myWebSocketHandler;

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
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, i, valueData, (key) -> FanInfoInit.GOLDEN_ID_MAP.get(key));
            list.add(dto);
        }
        for (FanRuntimeDTO dto : list) {
            BigDecimal obj = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_POWER + dto.getNumber());
            BigDecimal powerGeneration = null == obj ? new BigDecimal(0) : obj;
            dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration().subtract(powerGeneration));
        }
        return list;
    }

    @Override
    public void getRuntimeInfos(String user) throws Exception {
        if (GoldenUtil.servers.containsKey(user)) {
            startTimer(user);
            return;
        }
        if (MyWebSocketHandler.GROUP_RUNTIME.keySet().size() > 0) {
            int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            List<FanRuntimeDTO> list = new ArrayList<>(10);
            for (int i = 1; i <= 63; i++) {
                FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
                fanRuntimeDto.setNumber(i);
                list.add(fanRuntimeDto);
            }
            try {
                GoldenUtil.subscribeSnapshots(user, ids, (data) -> {
                    try {
                        if (MyWebSocketHandler.GROUP_RUNTIME.keySet().size() > 0 && GoldenUtil.servers.containsKey(user)) {
                            long l0 = System.currentTimeMillis();
//                            DataConvertUtils.convertAndSend(data);
//                            long l01 = System.currentTimeMillis();
//                            log.info("发送kafka耗时{}",l01-l0);
                            List<FanRuntimeDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data, (key) -> FanInfoInit.GOLDEN_ID_MAP.get(key));
                            if (null != dtos) {
                                for (FanRuntimeDTO dto : dtos) {
                                    BigDecimal obj = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_POWER + dto.getNumber());
                                    BigDecimal powerGeneration = null == obj ? new BigDecimal(0) : obj;
                                    dto.setMonthlyPowerGeneration(dto.getMonthlyPowerGeneration().subtract(powerGeneration));
                                }
                                String jsonString = JSONObject.toJSONString(dtos);
                                jsonString = WebSocketConstant.MAIN_PAGE_RUNTIME + WebSocketConstant.PATTERN + jsonString;
                                try {
                                    myWebSocketHandler.sendGroupMessage(jsonString, MyWebSocketHandler.GROUP_RUNTIME.keySet());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            startTimer(user);
                            long l1 = System.currentTimeMillis();
                            log.info("实时数据，处理时间{}，消息时间{}", l1 - l0, data[0].getDate());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != TimerUtil.TIMER_MAP.get(user)) {
                            TimerUtil.stop(user);
                        }
                        GoldenUtil.cancel(user);
                        myWebSocketHandler.sendGroupMessage("重新订阅", MyWebSocketHandler.GROUP_RUNTIME.keySet());
                        log.info("回调函数内部触发取消操作");
                    }
                });
            } catch (SocketException e) {
                log.info("golden连接失败，重连后取消之前所有连接");
                GoldenUtil.servers.clear();
                e.printStackTrace();
                myWebSocketHandler.sendAllMessage("重新订阅");
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
        List<FanStatisticsDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, valueData, (key) -> FanInfoInit.GOLDEN_ID_MAP.get(key));
        if (null != dtos) {
            dto = injecctPorerties(dtos);
        }
        return dto;
    }

    @Override
    public void getStatistics(String user) throws Exception {
        if (GoldenUtil.servers.containsKey(user)) {
            startTimer(user);
            return;
        }
        if (MyWebSocketHandler.GROUP_STATISTICS.keySet().size() > 0) {
            int[] ids = {1, 2, 13, 14};
            List<FanStatisticsDTO> list = new ArrayList<>();
            for (int i = 1; i <= 63; i++) {
                list.add(new FanStatisticsDTO());
            }
            try {
                GoldenUtil.subscribeSnapshots(user, ids, (data) -> {
                    try {
                        if (MyWebSocketHandler.GROUP_STATISTICS.keySet().size() > 0 && GoldenUtil.servers.containsKey(user)) {
                            long l0 = System.currentTimeMillis();
                            List<FanStatisticsDTO> dtos = InjectPropertiesUtil.injectByAnnotation(list, data, (key) -> FanInfoInit.GOLDEN_ID_MAP.get(key));
                            if (null != dtos) {
                                FanStatisticsDTO dto = injecctPorerties(dtos);
                                String jsonString = JSONObject.toJSONString(dto);
                                jsonString = WebSocketConstant.MAIN_PAGE_STATISTICS + WebSocketConstant.PATTERN + jsonString;
                                try {
                                    myWebSocketHandler.sendGroupMessage(jsonString, MyWebSocketHandler.GROUP_STATISTICS.keySet());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            startTimer(user);
                            long l1 = System.currentTimeMillis();
                            log.info("统计数据，处理时间{}，消息时间{}", l1 - l0, data[0].getDate());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != TimerUtil.TIMER_MAP.get(user)) {
                            TimerUtil.stop(user);
                        }
                        GoldenUtil.servers.remove(user);
                        myWebSocketHandler.sendGroupMessage("重新订阅", MyWebSocketHandler.GROUP_STATISTICS.keySet());
                        log.info("回调函数内部触发取消操作");
                    }
                });
            } catch (SocketException e) {
                log.info("golden连接失败，重连后取消之前所有连接");
                e.printStackTrace();
                GoldenUtil.servers.clear();
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
        for (int i = 1; i <= 63; i++) {
            FanRuntimeDTO fanRuntimeDto = new FanRuntimeDTO();
            fanRuntimeDto.setNumber(i);
            fanRuntimeDto.setRunningStatusString("正常运行");
            FanRuntimeDTO dto = InjectPropertiesUtil.injectByAnnotation(fanRuntimeDto, i, valueData, (key) -> FanInfoInit.GOLDEN_ID_MAP.get(key));
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
            valueData = GoldenUtil.getSnapshots(ids);
        } catch (SocketException e) {
            log.info("golden连接失败，重连后取消之前所有连接");
            GoldenUtil.servers.clear();
            myWebSocketHandler.sendAllMessage("重新订阅");
            return null;
        }
        return valueData;
    }

    private FanStatisticsDTO injecctPorerties(List<FanStatisticsDTO> dtos) {
        BigDecimal init = BigDecimal.ZERO;
        BigDecimal obj1 = FanInfoInit.POWER_MAP.get(FanConstant.DAILY_POWER_ALL);
        BigDecimal dailyPowerGeneration = null == obj1 ? init : obj1;
        BigDecimal obj2 = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_POWER_ALL);
        BigDecimal monthlyPowerGeneration = null == obj2 ? init : obj2;
        BigDecimal obj3 = FanInfoInit.POWER_MAP.get(FanConstant.ANNUAL_POWER_ALL);
        BigDecimal annualPowerGeneration = null == obj3 ? init : obj3;
        BigDecimal obj4 = FanInfoInit.POWER_MAP.get(FanConstant.DAILY_ONLINE_ALL);
        BigDecimal dailyOnlinePower = null == obj4 ? init : obj4;
        BigDecimal obj5 = FanInfoInit.POWER_MAP.get(FanConstant.MONTHLY_ONLINE_ALL);
        BigDecimal monthlyOnlinePower = null == obj5 ? init : obj5;
        BigDecimal obj6 = FanInfoInit.POWER_MAP.get(FanConstant.ANNUAL_ONLINE_ALL);
        BigDecimal annualOnlinePower = null == obj6 ? init : obj6;

        BigDecimal activePower = dtos.stream().map(FanStatisticsDTO::getActivePower).reduce(init, BigDecimal::add);
        BigDecimal windVelocity = dtos.stream().filter(e -> null != e.getAverageWindVelocity()).map(FanStatisticsDTO::getAverageWindVelocity).reduce(init, BigDecimal::add);
        long count = dtos.stream().filter(e -> null != e.getAverageWindVelocity()).count();
        BigDecimal averageWindVelocity = windVelocity.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
        BigDecimal energyOutput = dtos.stream().map(FanStatisticsDTO::getEnergyOutput).reduce(init, BigDecimal::add);
        BigDecimal reverseActivePower = dtos.stream().map(FanStatisticsDTO::getReverseActivePower).reduce(init, BigDecimal::add);

        FanStatisticsDTO dto = new FanStatisticsDTO();
        dto.setNum(63);
        dto.setCapacity(new BigDecimal(63 * 4));
        dto.setActivePower(activePower);
        dto.setAverageWindVelocity(averageWindVelocity);
        dto.setDailyOnlinePower(reverseActivePower.subtract(dailyOnlinePower));
        dto.setMonthlyOnlinePower(reverseActivePower.subtract(monthlyOnlinePower));
        dto.setAnnualOnlinePower(reverseActivePower.subtract(annualOnlinePower));
        dto.setDailyPowerGeneration(energyOutput.subtract(dailyPowerGeneration));
        dto.setMonthlyPowerGeneration(energyOutput.subtract(monthlyPowerGeneration));
        dto.setAnnualPowerGeneration(energyOutput.subtract(annualPowerGeneration));
        return dto;
    }

    /**
     * 开启定时任务
     */
    private void startTimer(String user) {
        TimerUtil.start(new TimerTask() {
            @Override
            public void run() {
                GoldenUtil.cancel(user);
                if ("runtime".equals(user)) {
                    try {
                        myWebSocketHandler.sendGroupMessage("重新订阅", MyWebSocketHandler.GROUP_STATISTICS.keySet());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        myWebSocketHandler.sendGroupMessage("重新订阅", MyWebSocketHandler.GROUP_STATISTICS.keySet());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                log.info("定时任务取消golden连接,{}", user);
            }
        }, user);
    }
}
