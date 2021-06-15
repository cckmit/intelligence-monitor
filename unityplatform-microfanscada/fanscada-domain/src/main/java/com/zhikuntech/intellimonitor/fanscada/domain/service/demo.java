package com.zhikuntech.intellimonitor.fanscada.domain.service;

import com.rtdb.api.callbackInter.RSDataChange;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanBaseInfo;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 滕楠
 * @className demo
 * @create 2021/6/11 16:24
 **/
public class demo {



    public static void main(String[] args) {
        GoldenUtil goldenUtil = new GoldenUtil();

        int[] a ={21,22,23,24};

        try {
            List<ValueData> snapshots = goldenUtil.getSnapshots(a);

            FanBaseInfo fanBaseInfo1 = InjectPropertiesUtil.injectByAnnotationDoubleToBigDecimal(new FanBaseInfo(), snapshots);

            System.out.println(fanBaseInfo1);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}