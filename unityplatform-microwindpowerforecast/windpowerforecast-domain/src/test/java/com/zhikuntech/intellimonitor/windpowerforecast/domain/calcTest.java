package com.zhikuntech.intellimonitor.windpowerforecast.domain;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.DqCalcUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.ErmseAggregateCalc;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class calcTest {
    @Test public void
    test1() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("234.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("250.81500");
        dqProduce.add(b1);
        dqProduce.add(b2);
        BigDecimal foreset=new BigDecimal("234.81500");//短期只会有一条数据;
        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("100.0000");//真实功率数据
        zrCapsProduce.add(c1);
        BigDecimal cap=new BigDecimal("168.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("252.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }

    @Test public void
    test2() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("234.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("250.81500");
        BigDecimal b3=new BigDecimal("100.81500");
        dqProduce.add(b1);
        dqProduce.add(b2);
        dqProduce.add(b3);
        BigDecimal foreset=new BigDecimal("234.81500");//短期只会有一条数据;
        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("150.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("150.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("150.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        zrCapsProduce.add(c3);
        BigDecimal cap=new BigDecimal("168.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("252.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }


    @Test public void
    test3() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("210.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("250.81500");
        BigDecimal b3=new BigDecimal("100.81500");
        dqProduce.add(b1);
        dqProduce.add(b2);
        dqProduce.add(b3);
        BigDecimal foreset=new BigDecimal("234.81500");//短期只会有一条数据;
        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("100.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("120.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("130.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        zrCapsProduce.add(c3);
        BigDecimal cap=new BigDecimal("118.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("200.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }

    @Test public void
    test4() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("100.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("220.81500");
        BigDecimal b3=new BigDecimal("140.81500");
        dqProduce.add(b1);
        dqProduce.add(b2);
        dqProduce.add(b3);
        BigDecimal foreset=new BigDecimal("234.81500");//短期只会有一条数据;
        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("180.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("220.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("180.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        zrCapsProduce.add(c3);
        BigDecimal cap=new BigDecimal("228.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("250.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }

    @Test public void
    test5() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("100.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("220.81500");
        BigDecimal b3=new BigDecimal("140.81500");
        BigDecimal b4=new BigDecimal("140.81500");
        BigDecimal b5=new BigDecimal("140.81500");
        BigDecimal b6=new BigDecimal("140.81500");
        BigDecimal b7=new BigDecimal("140.81500");
        BigDecimal b8=new BigDecimal("140.81500");

        dqProduce.add(b1);
        dqProduce.add(b2);
        dqProduce.add(b3);
        dqProduce.add(b4);
        dqProduce.add(b5);
        dqProduce.add(b6);
        dqProduce.add(b7);
        dqProduce.add(b8);

        BigDecimal foreset=new BigDecimal("200.81500");//短期只会有一条数据;

        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("180.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("220.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("180.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);

        BigDecimal cap=new BigDecimal("228.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("250.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }

    @Test public void
    test6() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("100.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("220.81500");
        BigDecimal b3=new BigDecimal("110.81500");
        BigDecimal b4=new BigDecimal("120.81500");
        BigDecimal b5=new BigDecimal("130.81500");
        BigDecimal b6=new BigDecimal("140.81500");
        BigDecimal b7=new BigDecimal("156.81500");
        BigDecimal b8=new BigDecimal("170.81500");

        dqProduce.add(b1);
        dqProduce.add(b2);
        dqProduce.add(b3);
        dqProduce.add(b4);
        dqProduce.add(b5);
        dqProduce.add(b6);
        dqProduce.add(b7);
        dqProduce.add(b8);

        BigDecimal foreset=new BigDecimal("200.81500");//短期只会有一条数据;

        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("180.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("220.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("380.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);

        BigDecimal cap=new BigDecimal("278.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("250.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }

    @Test public void
    test7() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("100.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("220.81500");
        BigDecimal b3=new BigDecimal("110.81500");
        BigDecimal b4=new BigDecimal("120.81500");
        BigDecimal b5=new BigDecimal("130.81500");
        BigDecimal b6=new BigDecimal("140.81500");
        BigDecimal b7=new BigDecimal("156.81500");
        BigDecimal b8=new BigDecimal("1111111.81500");

        dqProduce.add(b1);
        dqProduce.add(b2);
        dqProduce.add(b3);
        dqProduce.add(b4);
        dqProduce.add(b5);
        dqProduce.add(b6);
        dqProduce.add(b7);
        dqProduce.add(b8);
        for (int i=0;i<96;i++){
            dqProduce.add(b8);
        }
        BigDecimal foreset=new BigDecimal("200.81500");//短期只会有一条数据;

        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("11180.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("22110.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("38110.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);

        BigDecimal cap=new BigDecimal("2781.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("2510.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }

    @Test public void
    test8() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("100.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("220.81500");
        BigDecimal b3=new BigDecimal("110.81500");
        BigDecimal b4=new BigDecimal("120.81500");
        BigDecimal b5=new BigDecimal("130.81500");
        BigDecimal b6=new BigDecimal("140.81500");
        BigDecimal b7=new BigDecimal("156.81500");
        BigDecimal b8=new BigDecimal("1111111.81500");

        dqProduce.add(b1);
        dqProduce.add(b2);
        dqProduce.add(b3);
        dqProduce.add(b4);
        dqProduce.add(b5);
        dqProduce.add(b6);
        dqProduce.add(b7);
        dqProduce.add(b8);
        for (int i=0;i<96;i++){
            dqProduce.add(b8);
        }
        BigDecimal foreset=new BigDecimal("200.81500");//短期只会有一条数据;

        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("11180.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("22110.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("38110.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        zrCapsProduce.add(c3);
        zrCapsProduce.add(c3);
        for (int i=0;i<96;i++){
            zrCapsProduce.add(c3);
        }
        BigDecimal cap=new BigDecimal("2781.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("2510.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }

    @Test public void
    test9() {
        List<ErmseAggregateCalc> aggrs= new ArrayList<>();
        LocalDateTime time=LocalDateTime.now();
        ErmseAggregateCalc a=new ErmseAggregateCalc();
        List<BigDecimal> dqProduce=new ArrayList<>();
        BigDecimal b1=new BigDecimal("100.81500");//短期/超短期预测
        BigDecimal b2=new BigDecimal("220.81500");
        BigDecimal b3=new BigDecimal("110.81500");
        BigDecimal b4=new BigDecimal("120.81500");
        BigDecimal b5=new BigDecimal("130.81500");
        BigDecimal b6=new BigDecimal("140.81500");
        BigDecimal b7=new BigDecimal("156.81500");
        BigDecimal b8=new BigDecimal("250.81500");

        for (int i=0;i<96;i++){
            dqProduce.add(b8);
        }
        BigDecimal foreset=new BigDecimal("200.81500");//短期只会有一条数据;

        List<BigDecimal> zrCapsProduce = new ArrayList<>();
        BigDecimal c1=new BigDecimal("11180.0000");//真实功率数据
        BigDecimal c2=new BigDecimal("22110.0000");//真实功率数据
        BigDecimal c3=new BigDecimal("38110.0000");//真实功率数据
        zrCapsProduce.add(c1);
        zrCapsProduce.add(c2);
        for (int i=0;i<96;i++){
            zrCapsProduce.add(c3);
        }
        BigDecimal cap=new BigDecimal("271.00000");//短期/超短期功率计算时使用的容量
        BigDecimal capForAssess=new BigDecimal("250.00000");//计算考核结果时的容量
        a.setEventTime(time);
        a.setDqProduce(dqProduce);
        a.setForeset(foreset);
        a.setZrCapsProduce(zrCapsProduce);
        a.setCap(cap);
        a.setCapForAssess(capForAssess);
        aggrs.add(a);
        System.out.println(DqCalcUtils.calcErmse(aggrs));
        System.out.println(DqCalcUtils.calcEMAE(aggrs));
        System.out.println(DqCalcUtils.calcMaxe(aggrs));
        System.out.println(DqCalcUtils.calcAboutR(aggrs));
        System.out.println(DqCalcUtils.calcAboutR2(aggrs));
    }
}