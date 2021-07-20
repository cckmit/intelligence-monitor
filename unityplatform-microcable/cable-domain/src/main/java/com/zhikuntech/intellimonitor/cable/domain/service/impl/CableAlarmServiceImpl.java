package com.zhikuntech.intellimonitor.cable.domain.service.impl;

import com.rtdb.api.model.RtdbData;
import com.zhikuntech.intellimonitor.cable.domain.dto.CableStressAlarmDTO;
import com.zhikuntech.intellimonitor.cable.domain.dto.CableTemperatureAlarmDTO;
import com.zhikuntech.intellimonitor.cable.domain.query.AlarmQuery;
import com.zhikuntech.intellimonitor.cable.domain.query.CableIdQuery;
import com.zhikuntech.intellimonitor.cable.domain.service.CableAlarmService;
import com.zhikuntech.intellimonitor.cable.domain.service.CableRunTimeService;
import com.zhikuntech.intellimonitor.cable.domain.utils.CableIdCalc;
import com.zhikuntech.intellimonitor.cable.domain.utils.MathUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sun.activation.registries.LogSupport.log;

@Service
@Slf4j
public class CableAlarmServiceImpl implements CableAlarmService {
    /**
     * 获取当前报警海缆分段的一段时间的温度数据
     */
    @Override
    public List<CableTemperatureAlarmDTO> getAlarmTemperature(CableIdQuery query) throws Exception {
        List<CableTemperatureAlarmDTO> result=new ArrayList<>();
        AlarmQuery alarmQuery=queryToQuery(query);
        List<RtdbData> list = getListByQuery(alarmQuery);
        int id=alarmQuery.getId();
        if (list.isEmpty()){
            return result;
        }
        for (RtdbData data : list){
            CableTemperatureAlarmDTO cableTemperatureAlarmDTO=new CableTemperatureAlarmDTO();
            cableTemperatureAlarmDTO.setNumber(id);
            cableTemperatureAlarmDTO.setDate(data.getDate());
            BigDecimal temperature= MathUtil.getBigDecimal(data.getValue());
            cableTemperatureAlarmDTO.setTemperature(temperature);
            result.add(cableTemperatureAlarmDTO);
        }
        return result;
    }
    /**
     * 获取当前报警海缆分段 报警时间 的 整条海缆 的温度数据
     */
    @Override
    public List<CableTemperatureAlarmDTO> getAlarmAllTemperature(CableIdQuery query) throws Exception {
        List<CableTemperatureAlarmDTO> result=new ArrayList<>();
        if (Objects.isNull(query)){
            return result;
        }
        int id = queryToQuery(query).getId();
        int[] ids=findCableById(id);
        String date=query.getDate();
        if (Objects.isNull(date)){
            return result;
        }
        Date time=stringToDate(date);
        List<RtdbData> list = getCrossSectionValues(ids,time);
        int j=1;
        assert list != null;
        for (RtdbData i : list){
            BigDecimal bigDecimal= MathUtil.getBigDecimal(i.getValue());
            CableTemperatureAlarmDTO cableTemperatureAlarmDTO=new CableTemperatureAlarmDTO();
            cableTemperatureAlarmDTO.setTemperature(bigDecimal);
            cableTemperatureAlarmDTO.setDate(time);
            cableTemperatureAlarmDTO.setNumber(j);
            j++;
            result.add(cableTemperatureAlarmDTO);
        }
        return result;
    }
    /**
     * 获取当前报警海缆分段的一段时间的应力数据
     */
    @Override
    public List<CableStressAlarmDTO> getAlarmStress(CableIdQuery query) throws Exception {
        List<CableStressAlarmDTO> result=new ArrayList<>();
        AlarmQuery alarmQuery=queryToQuery(query);
        List<RtdbData> list = getListByQuery(alarmQuery);
        int id=alarmQuery.getId();
        for (RtdbData data : list){
            CableStressAlarmDTO cableStressAlarmDTO=new CableStressAlarmDTO();
            cableStressAlarmDTO.setNumber(id);
            cableStressAlarmDTO.setDate(data.getDate());
            BigDecimal temperature= MathUtil.getBigDecimal(data.getValue());
            cableStressAlarmDTO.setStress(temperature);
            result.add(cableStressAlarmDTO);
        }
        return result;
    }
    /**
     * 获取当前报警海缆分段 报警时间 的 整条海缆 的应力数据
     */
    @Override
    public List<CableStressAlarmDTO> getAlarmAllStress(CableIdQuery query) throws Exception {
        List<CableStressAlarmDTO> result=new ArrayList<>();
        if (Objects.isNull(query)){
            return result;
        }
        int id = queryToQuery(query).getId();
        int[] ids=findCableById(id);
        String date=query.getDate();
        if (Objects.isNull(date)){
            return result;
        }
        int j=1;
        Date time=stringToDate(date);
        List<RtdbData> list = getCrossSectionValues(ids,time);
        assert list != null;
        for (RtdbData i : list){
            BigDecimal bigDecimal= MathUtil.getBigDecimal(i.getValue());
            CableStressAlarmDTO cableStressAlarmDTO=new CableStressAlarmDTO();
            cableStressAlarmDTO.setStress(bigDecimal);
            cableStressAlarmDTO.setDate(time);
            cableStressAlarmDTO.setNumber(j);
            j++;
            result.add(cableStressAlarmDTO);
        }
        return result;
    }

    /**
     * 根据CableIdQuery 得到 AlarmQuery
     */
    public AlarmQuery queryToQuery(CableIdQuery query){
        AlarmQuery query1=new AlarmQuery();
        int queryId= CableIdCalc.numToId(query.getType(),query.getId(),query.getNum());
        query1.setId(queryId);
        query1.setDate(query.getDate());
        return query1;
    }

    /**
     * 根据query 在庚顿数据库查询出数据 查询点前后12小时的全部数据
     */
    public List<RtdbData> getListByQuery(AlarmQuery query) throws Exception {
        List<RtdbData> result=new ArrayList<>();
        if(Objects.isNull(query)){
            return result;
        }
        int id=query.getId();
        String date=query.getDate();
        if (Objects.isNull(date)){
            return result;
        }
        Date time=stringToDate(date);
        Date dateStart=dateRoll(time,-12);
        Date dateEnd=dateRoll(time,12);
        List<RtdbData> list=getArchivedValues(id,dateStart,dateEnd);
        if (list.isEmpty()){
            return result;
        }
        return list;
    }
    /**
     * 根据报警的id 查是哪条海缆的 写出查询庚顿数据库用的数组
     * A海缆温度点位8804-9803 14803-14842
     * B海缆温度点位9804-9932(1-129) 9933-10802(131-1000) 14843-14882(1001-1040)
     * C海缆温度点位10803-11802 14883-14922
     * A海缆应力点位11803-12802 14923-15484
     * B海缆应力点位12803-13802 15485-16046
     * C海缆应力点位13803-14802 16047-16608
     */
    public int[] findCableById(int id){
        int[] ids;
        if (Objects.isNull(id)){
            return new int[0];
        }
        if ((id>=8804 && id<=9803) || (id>=14803 && id<=14842)){
            int a=8804;
            int b=14803;
            ids=abToInts(a,b,1000);
        }else if ((id>9803 && id<10803) || (id>14842 && id<14883)){
            ids = new int[1040];
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
        }else if ((id>=10803 && id<=11802) || (id>=14883 && id<=14922)){
            int a=10803;
            int b=14883;
            ids=abToInts(a,b,1000);
        }else if ((id>=11803 && id<=12802)||(id>=14923 && id<=15484)){
            int a=11803;
            int b=14923;
            ids=abToInts(a,b,1562);
        } else if ((id>=12803 && id<=13802)||(id>=15485 && id<=16046)){
            int a=12803;
            int b=15485;
            ids=abToInts(a,b,1562);
        } else if ((id>=13803 && id<=14802)||(id>=16047 && id<=16608)){
            int a=13803;
            int b=16047;
            ids=abToInts(a,b,1562);
        } else{
            ids=new int[0];
            log("输入海缆id错误 没有查询结果");
        }
        return ids;
    }

    /**
     * 根据起始点位a,b 数量c写出查询庚顿数据库用的数组
     */
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

    /**
     * Object 转 BigDecimal
     */
    public static BigDecimal toBigDecimal(Object value) {
        BigDecimal bigDec = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                bigDec = (BigDecimal) value;
            } else if (value instanceof String) {
                bigDec = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                bigDec = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                bigDec = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Can Not make [" + value + "] into a BigDecimal.");
            }
        }
        return bigDec;
    }

    /**
     * Date 格式 加小时
     */
    public static Date dateRoll(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, i);
        Date newDate = c.getTime();
        return newDate;
    }

    /**
     * String 转成 Date 格式
     */
    public Date stringToDate(String str){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(str, pos);
        return date;
    }

    /**
     * 获取指定标签点一段时间内的历史存储值
     * "2021-07-02 15:40:00"
     */
    private List<RtdbData> getArchivedValues(int id, Date dateStart,Date dateEnd) throws Exception {
        List<RtdbData> valueData;
        try {
            valueData = GoldenUtil.getArchivedValues(id,dateStart,dateEnd);
        } catch (SocketException e) {
            CableAlarmServiceImpl.log.info("golden连接失败，重连后取消之前所有连接");
            GoldenUtil.servers.clear();
            //myWebSocketHandler.sendAllMessage("重新订阅");
            return null;
        }
        return valueData;
    }


    /**
     * 获取指定时间的整条海缆的数据
     * "2021-07-02 15:40:00"
     */
    private List<RtdbData> getCrossSectionValues(int[] ids,Date datetime) throws Exception {
        List<RtdbData> value;
        try {
            value = GoldenUtil.getCrossSectionValues(ids,datetime);
        } catch (SocketException e) {
            CableAlarmServiceImpl.log.info("golden连接失败，重连后取消之前所有连接");
            GoldenUtil.servers.clear();
            //myWebSocketHandler.sendAllMessage("重新订阅");
            return null;
        }
        return value;
    }
}
