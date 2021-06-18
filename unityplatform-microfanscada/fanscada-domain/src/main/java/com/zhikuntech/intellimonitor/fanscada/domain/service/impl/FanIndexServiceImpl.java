package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.core.commons.constant.FanConstant;
import com.zhikuntech.intellimonitor.fanscada.domain.constant.FanLoopNumber;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQueryList;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.GoldenIdQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
import com.zhikuntech.intellimonitor.fanscada.domain.utils.RedisUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanBaseInfoVO;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.LoopVO;
import com.zhikuntech.intellimonitor.fanscada.domain.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author 滕楠
 * @className FanIndexServiceImpl
 * @create 2021/6/15 11:22
 **/
@Service
@Slf4j
public class FanIndexServiceImpl implements FanIndexService {

    @Autowired
    private GoldenUtil goldenUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private BackendToGoldenService backendToGoldenService;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void getFanBaseInfoList(String username, List<Integer> windNumberList) throws Exception {

        //GoldenUtil goldenUtil = new GoldenUtil();
        //通过mapper获取庚顿数据的对应关系
        for (Integer integer : windNumberList) {
            //获取所有风场的number,封装其对应的数据字段ID
            //风速,转速,有功功率,无功功率,状态,日发电总量=有功能量输出,

        }
        if (WebSocketServer.clients.containsKey(username)) {
            int[] ids = {21, 22, 23, 24, 12, 101};//需要的风机数据(12:状态)101,总发电量  -当日0点的总发电量=日发电量

            List<FanBaseInfoVO> list = new ArrayList<>(10);
            for (int i = 1; i <= 10; i++) {
                FanBaseInfoVO fanBaseInfoVO = new FanBaseInfoVO();
                fanBaseInfoVO.setFanNumber(i + "");
                list.add(fanBaseInfoVO);
            }
            goldenUtil.subscribeSnapshots(username, ids, (data) -> {
                if (!WebSocketServer.clients.containsKey(username)) {
                    return;
                } else {
                    List<FanBaseInfoVO> dtos = InjectPropertiesUtil.injectByAnnotationForBigdecimal(list, data);
                    if (null != dtos) {

                        List<LoopVO> loopVOS = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            LoopVO loopVO1 = new LoopVO();
                            loopVO1.setLoopNumber("0000000000000" + i);
                            loopVO1.setFanBaseInfoVOS(dtos);
                            loopVOS.add(loopVO1);
                        }
                        String jsonString = JSONObject.toJSONString(loopVOS);
                        log.info(jsonString);
                        webSocketServer.sendMessage(jsonString, username);
                    }
                }
            });
        }
    }

    @Override
    public void getFanBaseInfoList(String username) throws Exception {
        //通过mapper获取庚顿数据的对应关系

        //获取所有风场的number,封装其对应的数据字段ID
        //风速,转速,有功功率,无功功率,状态,日发电总量=有功能量输出,
        List<Integer> idList = new ArrayList<>();
        idList.add(21);
        idList.add(22);
        idList.add(23);
        idList.add(24);
        idList.add(177);
        idList.add(101);
        //风机列表
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 2; i++) {
            list.add(i);
        }
        GoldenIdQuery query = new GoldenIdQuery();
        query.setDataIds(idList);
        List<Integer> goldenIds = backendToGoldenService.getGoldenIdByNumberAndId(query);

        int[] ints = goldenIds.stream().mapToInt(Integer::intValue).toArray();

        if (WebSocketServer.clients.containsKey(username)) {
            goldenUtil.subscribeSnapshots(username, ints, (data) -> {

                long l = System.currentTimeMillis();
                if (!WebSocketServer.clients.containsKey(username)) {
                    return;
                } else {
                    List<FanBaseInfoVO> fanBaseInfoVOList = new ArrayList<>();//63台风机
                    for (int i = 0; i < 63; i++) {
                        FanBaseInfoVO fanBaseInfoVO = new FanBaseInfoVO();
                        fanBaseInfoVO.setFanNumber(i+"");
                        fanBaseInfoVOList.add(fanBaseInfoVO);
                    }
                    List<FanBaseInfoVO> result = InjectPropertiesUtil.injectByAnnotationForBigdecimal(fanBaseInfoVOList, data);

                    List<FanBaseInfoVO> list1 = new ArrayList<>();
                    List<FanBaseInfoVO> list2 = new ArrayList<>();
                    List<FanBaseInfoVO> list3 = new ArrayList<>();
                    List<FanBaseInfoVO> list4 = new ArrayList<>();
                    List<FanBaseInfoVO> list5 = new ArrayList<>();
                    List<FanBaseInfoVO> list6 = new ArrayList<>();
                    List<FanBaseInfoVO> list7 = new ArrayList<>();
                    List<FanBaseInfoVO> list8 = new ArrayList<>();
                    List<FanBaseInfoVO> list9 = new ArrayList<>();
                    List<FanBaseInfoVO> list10 = new ArrayList<>();
                    if (result==null){
                        return;
                    }
                    for (FanBaseInfoVO fanBaseInfoVO : result) {
                        if (FanLoopNumber.LOOP1.contains(fanBaseInfoVO.getFanNumber())) {
                            list1.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP2.contains(fanBaseInfoVO.getFanNumber())) {
                            list2.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP3.contains(fanBaseInfoVO.getFanNumber())) {
                            list3.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP4.contains(fanBaseInfoVO.getFanNumber())) {
                            list4.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP5.contains(fanBaseInfoVO.getFanNumber())) {
                            list5.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP6.contains(fanBaseInfoVO.getFanNumber())) {
                            list6.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP7.contains(fanBaseInfoVO.getFanNumber())) {
                            list7.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP8.contains(fanBaseInfoVO.getFanNumber())) {
                            list8.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP9.contains(fanBaseInfoVO.getFanNumber())) {
                            list9.add(fanBaseInfoVO);
                        } else if (FanLoopNumber.LOOP10.contains(fanBaseInfoVO.getFanNumber())) {
                            list10.add(fanBaseInfoVO);
                        }
                    }
                    List<List<FanBaseInfoVO>> lists = new ArrayList<>();
                    lists.add(list1);
                    lists.add(list2);
                    lists.add(list3);
                    lists.add(list4);
                    lists.add(list5);
                    lists.add(list6);
                    lists.add(list7);
                    lists.add(list8);
                    lists.add(list9);
                    lists.add(list10);
                    List<LoopVO> resultList = new ArrayList<>();
                    int n = 1;
                    for (List<FanBaseInfoVO> fanBaseInfoVOS : lists) {
                        BigDecimal activePowerSum = BigDecimal.valueOf(0.0);
                        BigDecimal reactivePowerSum = BigDecimal.valueOf(0.0);
                        BigDecimal windSpeedSum = BigDecimal.valueOf(0.0);
                        BigDecimal dayEnergySum = BigDecimal.valueOf(0.0);
                        LoopVO loopVO = new LoopVO();
                        for (FanBaseInfoVO fanBaseInfoVO : fanBaseInfoVOS) {
                            BigDecimal activePower = fanBaseInfoVO.getActivePower();//有功功率
                            if (activePower == null) {
                                activePower = BigDecimal.valueOf(0.0);
                            }
                            BigDecimal reactivePower = fanBaseInfoVO.getReactivePower();
                            if (reactivePower == null) {
                                reactivePower = BigDecimal.valueOf(0.0);
                            }
                            BigDecimal windSpeed = fanBaseInfoVO.getWindSpeed();
                            if (windSpeed == null) {
                                windSpeed = BigDecimal.valueOf(0.0);
                            }
                            BigDecimal energy = fanBaseInfoVO.getEnergy();//总发电量
                            if (energy == null) {
                                energy = BigDecimal.valueOf(0.0);
                            }
                            //当日零点的总发电量
                            String string = redisUtil.get(FanConstant.DAILY_POWER+fanBaseInfoVO.getFanNumber()).toString();
                            BigDecimal v = BigDecimal.valueOf(Double.parseDouble("100"));
                            BigDecimal dayEnergy = energy.subtract(v);//日发电量

                            activePowerSum = activePowerSum.add(activePower);
                            reactivePowerSum = reactivePowerSum.add(reactivePower);
                            windSpeedSum = windSpeedSum.add(windSpeed);
                            dayEnergySum = dayEnergySum.add(dayEnergy);
                        }
                        loopVO.setLoopNumber("53"+n);
                        n++;
                        loopVO.setFanBaseInfoVOS(fanBaseInfoVOS);
                        loopVO.setActivePower(activePowerSum);
                        loopVO.setReactivePower(reactivePowerSum);
                        loopVO.setWindSpeedAvg(windSpeedSum.divide(BigDecimal.valueOf(fanBaseInfoVOS.size()), BigDecimal.ROUND_HALF_UP));
                        loopVO.setGeneratingCapacityForDay(dayEnergySum);
                        resultList.add(loopVO);
                    }
                    String jsonString = JSONObject.toJSONString(resultList);
                    log.info(jsonString);
                    webSocketServer.sendMessage(jsonString, username);
                    long l1 = System.currentTimeMillis();
                    System.out.println("############################"+(l1-l)+"========="+data[0].getDate().toString());
                }
            });
        }
    }
}