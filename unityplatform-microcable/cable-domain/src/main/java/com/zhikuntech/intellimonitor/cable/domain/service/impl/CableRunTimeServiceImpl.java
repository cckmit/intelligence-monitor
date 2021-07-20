package com.zhikuntech.intellimonitor.cable.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.cable.domain.dto.CableRunStressTimeDTO;
import com.zhikuntech.intellimonitor.cable.domain.dto.CableRunTimeTemperatureDTO;
import com.zhikuntech.intellimonitor.cable.domain.service.CableRunTimeService;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CableRunTimeServiceImpl implements CableRunTimeService {
    @Override
    public List<CableRunStressTimeDTO> getRuntimeStress1() throws Exception {
        //A海缆应力点位11803-12802 14923-15484
        int a=11803;
        int b=14923;
        int[] ids=abToInts(a,b,1562);
        return valueDateToStressList(ids);
    }

    @Override
    public List<CableRunStressTimeDTO> getRuntimeStress2() throws Exception {
        //B海缆应力点位12803-13802 15485-16046
        int a=12803;
        int b=15485;
        int[] ids=abToInts(a,b,1562);
        return valueDateToStressList(ids);
    }

    @Override
    public List<CableRunStressTimeDTO> getRuntimeStress3() throws Exception {
        //C海缆应力点位13803-14802 16047-16608
        int a=13803;
        int b=16047;
        int[] ids=abToInts(a,b,1562);
        return valueDateToStressList(ids);
    }

    @Override
    public List<CableRunStressTimeDTO> getRuntimeStressTest() throws Exception {
        int[] ids = new int[1562];//海缆应力点位 测试
        for (int j = 0 ; j <= 156 ; j++ ){
            ids[0]=1;
            if (j<156){
                for(int i = 1 ; i <= 10 ; i++ ){
                    ids[j*10+i] = i;
                }
            }else {
                ids[1560]=1;
                ids[1561]=2;
            }
        }
        return valueDateToStressList(ids);
    }

    @Override
    public List<CableRunTimeTemperatureDTO> getRuntimeTemperatureTest() throws Exception {
        int[] ids = new int[1040];//海缆温度点位 测试
        ids[0]=1;

        for (int j = 0 ; j <= 103 ; j++ ){
            if (j<103){
                for(int i = 1 ; i <= 10 ; i++ ){
                    ids[j*10+i] = i;
                }
            }else {
                for(int i = 1 ; i <=9 ; i++ ){
                    ids[j*10+i] = i;
                }
            }
        }
        return valueDateToTemperatureList(ids);
    }

    @Override
    public List<CableRunTimeTemperatureDTO> getRuntimeTemperature1() throws Exception {
        //A海缆温度点位8804-9803 14803-14842
        int a=8804;
        int b=14803;
        int[] ids=abToInts(a,b,1000);
        return valueDateToTemperatureList(ids);
    }

    @Override
    public List<CableRunTimeTemperatureDTO> getRuntimeTemperature2() throws Exception {
        int[] ids = new int[1040];//B海缆温度点位9804-9932(1-129) 9933-10802(131-1000) 14843-14882(1001-1040)  (2号海缆温度传感器130号 庚顿里没有这个点)
        int a=9804;
        int b=9933;
        int c=14843;
        for (int j=0 ; j<1040; j++){
            if (j<129){//0-128
               ids[j] = a;
                a++;
            } else if (j==129){//129 130号海缆
                ids[j] = 0;//先写成1
            }else if (j>129 && j<1000){//130-999 131号到1000号
                ids[j] = b;
                b++;
            }else{
                ids[j] = c;
                c++;
            }
        }
        return valueDateToTemperatureList(ids);
    }

    @Override
    public List<CableRunTimeTemperatureDTO> getRuntimeTemperature3() throws Exception {
        //C海缆温度点位10803-11802 14883-14922
        int a=10803;
        int b=14883;
        int[] ids=abToInts(a,b,1000);
        return valueDateToTemperatureList(ids);
    }

    public synchronized int[] abToInts(int a,int b,int c){
        int[] ids = new int[c];
        for (int j=0 ; j<ids.length; j++){
            if (j<1000){
                ids[j] = a;
                a++;
            } else{
                ids[j] = b;
                b++;
            }
        }
        return ids;
    }

    public synchronized List<CableRunTimeTemperatureDTO> valueDateToTemperatureList(int[] ids) throws Exception{
        List<ValueData> valueData = getSnapShops(ids);
        List<CableRunTimeTemperatureDTO> list=new ArrayList<>();
        if (null == valueData){
            return list;
        }
        int i=1;
        for (ValueData a:valueData){
            CableRunTimeTemperatureDTO cableRunTimeTemperatureDTO=new CableRunTimeTemperatureDTO();
            cableRunTimeTemperatureDTO.setNumber(i);
            BigDecimal b= BigDecimal.valueOf(a.getValue());
            cableRunTimeTemperatureDTO.setTemperature(b);
            list.add(cableRunTimeTemperatureDTO);
            i++;
        }
        return list;
    }

    public synchronized List<CableRunStressTimeDTO> valueDateToStressList(int[] ids) throws Exception{
        List<ValueData> valueData = getSnapShops(ids);
        List<CableRunStressTimeDTO> list=new ArrayList<>();
        if (null == valueData){
            return list;
        }
        int i=1;
        for (ValueData a:valueData){
            CableRunStressTimeDTO cableRunStressTimeDTO=new CableRunStressTimeDTO();
            cableRunStressTimeDTO.setNumber(i);
            BigDecimal b= BigDecimal.valueOf(a.getValue());
            cableRunStressTimeDTO.setStress(b);
            list.add(cableRunStressTimeDTO);
            i++;
        }
        return list;
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
            //myWebSocketHandler.sendAllMessage("重新订阅");
            return null;
        }
        return valueData;
    }
}
