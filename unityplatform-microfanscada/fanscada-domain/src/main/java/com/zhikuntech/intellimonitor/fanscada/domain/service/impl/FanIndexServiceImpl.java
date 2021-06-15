package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanIndexService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanBaseInfo;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.Loop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 滕楠
 * @className FanIndexServiceImpl
 * @create 2021/6/15 11:22
 **/
@Service
public class FanIndexServiceImpl implements FanIndexService {

    @Autowired
    //private GoldenUtil goldenUtil;


    @Override
    public BaseResponse<List<Loop>> getFanBaseInfoList() {

        GoldenUtil goldenUtil = new GoldenUtil();
        //通过mapper获取庚顿数据的对应关系

        int[] a ={21,22,23,24};
        List<ValueData> snapshots = null;
        try {
            snapshots = goldenUtil.getSnapshots(a);
        } catch (Exception e) {
            return BaseResponse.failure(ResultCode.PARAMETER_ERROR,"参数错误");
        }
        FanBaseInfo fanBaseInfo = new FanBaseInfo();
        fanBaseInfo = InjectPropertiesUtil.injectByAnnotationDoubleToBigDecimal(fanBaseInfo, snapshots);
        if (fanBaseInfo == null) {
            return BaseResponse.failure(ResultCode.PARAMETER_ERROR, "参数错误");
        }

        List<FanBaseInfo> fanBaseInfoList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            //todo 模拟数据
            fanBaseInfo.setFanNumber(i + "");
            fanBaseInfoList.add(fanBaseInfo);
        }
        Loop loop = new Loop();

        BigDecimal windSpeedSum = BigDecimal.valueOf(0.0);
        BigDecimal rotateSpeedSum = BigDecimal.valueOf(0.0);
        BigDecimal activePowerSum = BigDecimal.valueOf(0.0);
        BigDecimal reactivePowerSum = BigDecimal.valueOf(0.0);

        for (FanBaseInfo baseInfo : fanBaseInfoList) {
            BigDecimal windSpeed = baseInfo.getWindSpeed();
            windSpeedSum = windSpeedSum.add(windSpeed);

            BigDecimal rotateSpeed = baseInfo.getRotateSpeed();
            rotateSpeedSum = rotateSpeedSum.add(rotateSpeed);

            BigDecimal activePower = baseInfo.getActivePower();
            activePowerSum = activePowerSum.add(activePower);

            BigDecimal reactivePower = baseInfo.getReactivePower();
            reactivePowerSum = reactivePowerSum.add(reactivePower);
        }
        loop.setFanBaseInfos(fanBaseInfoList);
        loop.setActivePower(activePowerSum);
        loop.setWindSpeedAvg(windSpeedSum.divide(BigDecimal.valueOf(fanBaseInfoList.size()),2, RoundingMode.HALF_UP));
        loop.setReactivePower(reactivePowerSum);
        //todo
        loop.setSwitchLocation("");
        loop.setGeneratingCapacityForDay(BigDecimal.valueOf(99999));

        List<Loop> loopList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            loop.setLoopNumber(i+"");
            loopList.add(loop);
        }

        return BaseResponse.success(loopList);
    }
}