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
import java.util.Random;

@Service
@Slf4j
public class CableRunTimeServiceImpl implements CableRunTimeService {
    @Override
    public List<CableRunStressTimeDTO> getRuntimeStress1() throws Exception {
        int[] ids = new int[1562];
        for (int j=0;j<=1561; j++){
            if(j<1000){
                for(int i =11803;i<=12802;i++){
                    ids[j] = i;
                }
            }else{
                for (int i=14803;i<=15484;i++){
                    ids[j] =i;
                }
            }
        }
        return valueDateToStressList(ids);
    }

    @Override
    public List<CableRunStressTimeDTO> getRuntimeStress2() throws Exception {
        int[] ids = new int[1562];
        for (int j=0;j<=1561; j++){
            if(j<1000){
                for(int i =12803;i<=13802;i++){
                    ids[j] = i;
                }
            }else{
                for (int i=15485;i<=16046;i++){
                    ids[j] =i;
                }
            }
        }
        return valueDateToStressList(ids);
    }

    @Override
    public List<CableRunStressTimeDTO> getRuntimeStress3() throws Exception {
        int[] ids = new int[1562];
        for (int j=0;j<=1561; j++){
            if(j<1000){
                for(int i =13803;i<=14802;i++){
                    ids[j] = i;
                }
            }else{
                for (int i=16047;i<=16608;i++){
                    ids[j] =i;
                }
            }
        }
        return valueDateToStressList(ids);
    }

    @Override
    public List<CableRunStressTimeDTO> getRuntimeStressTest() throws Exception {
        int[] ids = new int[1562];
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
        int[] ids = new int[1040];
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
        int[] ids = new int[1040];
        for (int j=0;j<=1040; j++){
            if(j<1000){
                for(int i =8804;i<=9803;i++){
                    ids[j] = i;
                }
            }else{
                for (int i=14803;i<=14842;i++){
                    ids[j] =i;
                }
            }
        }
        return valueDateToTemperatureList(ids);
    }

    @Override
    public List<CableRunTimeTemperatureDTO> getRuntimeTemperature2() throws Exception {
        int[] ids = new int[1040];
        for (int j=0;j<=1040; j++){
            if(j<1000){
                for(int i =9804;i<=10802;i++){
                    ids[j] = i;
                }
            }else{
                for (int i=14843;i<=14882;i++){
                    ids[j] =i;
                }
            }
        }
        return valueDateToTemperatureList(ids);
    }

    @Override
    public List<CableRunTimeTemperatureDTO> getRuntimeTemperature3() throws Exception {
        int[] ids = new int[1040];
        for (int j=0;j<=1040; j++){
            if(j<1000){
                for(int i =10803;i<=11802;i++){
                    ids[j] = i;
                }
            }else{
                for (int i=14883;i<=14922;i++){
                    ids[j] =i;
                }
            }
        }
        return valueDateToTemperatureList(ids);
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
            BigDecimal b=new BigDecimal(a.getValue());
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
            BigDecimal b=new BigDecimal(a.getValue());
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
