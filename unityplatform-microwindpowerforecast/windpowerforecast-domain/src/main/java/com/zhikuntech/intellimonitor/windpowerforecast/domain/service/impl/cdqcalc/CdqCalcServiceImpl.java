package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.cdqcalc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.cdqcalc.CdqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.AssessCalcUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.CalcCommonUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.DqCalcUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.ErmseAggregateCalc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 该实现关注计算,不关注触发的时机
 * <p>
 *     外部服务必须判断是否已经符合了触发的条件
 *     --
 *     传递参数, 例如:
 *     [2021-06-22 01:15:00] - [2021-06-22 01:30:00]
 *     获取的真实数据的时间为 [2021-06-22 01:15:00] - [2021-06-22 01:30:00]
 *     获取短期数据的时间为  [2021-06-22 01:30:00] - [2021-06-22 01:45:00]    headerDate为[2021-06-22 01:30:00]
 *     获取真实容量的时间为  [2021-06-22 01:15:00] - [2021-06-22 01:30:00]
 * </p>
 *
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CdqCalcServiceImpl implements CdqCalcService {


    /**
     * 时间基准
     */
    private final IWfTimeBaseService timeBaseService;

    /**
     * 真实功率
     */
    private final IWfDataZrService zrService;

    /**
     * 容量数据
     */
    private final IWfDataCapacityService capacityService;

    /**
     * 超短期预测数据
     */
    private final IWfDataCdqService cdqService;

    /**
     * 超短期功率分析
     */
    private final IWfAnalyseCdqService analyseCdqService;

    /**
     * 日考核结果
     */
    private final IWfAssessDayService assessDayService;



    @Override public void calcData(String bg, String end, String headerDate) {
        /*
            bg  ->  yyyy-MM-dd HH:mm:ss
            end ->  yyyy-MM-dd HH:mm:ss
            headerDate  ->  yyyy-MM-dd HH:mm:ss
         */


        // 时间基准数据
        QueryWrapper<WfTimeBase> timeBaseQueryWrapper = new QueryWrapper<>();
        timeBaseQueryWrapper.gt("date_time", bg);
        timeBaseQueryWrapper.le("date_time", end);
        timeBaseQueryWrapper.eq("time_ratio", 15);
        List<WfTimeBase> timeBaseList = timeBaseService.getBaseMapper().selectList(timeBaseQueryWrapper);
        if (CollectionUtils.isEmpty(timeBaseList)) {
            log.warn("时间区间[{}]-[{}]无时间基准数据.", bg, end);
            return;
        }

        // 真实功率数据
        QueryWrapper<WfDataZr> zrQueryWrapper = new QueryWrapper<>();
        zrQueryWrapper.gt("event_date_time", bg);
        zrQueryWrapper.le("event_date_time", end);
        List<WfDataZr> zrList = zrService.getBaseMapper().selectList(zrQueryWrapper);
        if (CollectionUtils.isEmpty(zrList)) {
            log.warn("时间区间[{}]-[{}]无真实功率数据.", bg, end);
            return;
        }
        Map<String, List<WfDataZr>> zrGroup = zrList.stream().collect(Collectors.groupingBy(p -> CalcCommonUtils.timePostRangeProcess.apply(p.getEventDateTime())));


        // 容量数据
        QueryWrapper<WfDataCapacity> capacityQueryWrapper = new QueryWrapper<>();
        capacityQueryWrapper.gt("event_date_time", bg);
        capacityQueryWrapper.le("event_date_time", end);
        List<WfDataCapacity> capacityList = capacityService.getBaseMapper().selectList(capacityQueryWrapper);
        if (CollectionUtils.isEmpty(capacityList)) {
            log.warn("时间区间[{}]-[{}]无容量数据.", bg, end);
            return;
        }

        Map<String, WfDataCapacity> capacityMap = capacityList.stream()
                .collect(Collectors.toMap(p -> CalcCommonUtils.localDateTimeStringFunction.apply(p.getEventDateTime()), p -> p, (p1, p2) -> p2));


        // 超短期功率数据
        LocalDateTime ltBg = TimeProcessUtils.parseLocalDateTimeWithSecondPattern(bg);
        ltBg = ltBg.plusMinutes(15);
        String ltBgStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(ltBg);
        LocalDateTime ltEnd = TimeProcessUtils.parseLocalDateTimeWithSecondPattern(end);
        ltEnd = ltEnd.plusMinutes(15);
        String ltEndStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(ltEnd);
        QueryWrapper<WfDataCdq> cdqQueryWrapper = new QueryWrapper<>();
        cdqQueryWrapper.gt("event_date_time", ltBgStr);
        cdqQueryWrapper.le("event_date_time", ltEndStr);
        cdqQueryWrapper.eq("header_date", headerDate);
        List<WfDataCdq> cdqList = cdqService.getBaseMapper().selectList(cdqQueryWrapper);
        if (CollectionUtils.isEmpty(cdqList)) {
            log.warn("时间区间[{}]-[{}]无超短期预测数据.", bg, end);
            return;
        }
        Map<String, List<WfDataCdq>> cdqGorup = cdqList.stream().collect(Collectors.groupingBy(p -> CalcCommonUtils.timePostRangeProcess.apply(p.getEventDateTime())));

        Map<String, WfDataCdq> cdqMap = cdqList.stream().collect(Collectors.toMap(p -> CalcCommonUtils.localDateTimeStringFunction.apply(p.getEventDateTime()), p -> p, (p1, p2) -> p2));


        /*
            按照指定时间分组数据
            此处为: 15分钟
         */
        List<ErmseAggregateCalc> aggregateCalcList = new ArrayList<>();
        for (WfTimeBase timeBase : timeBaseList) {
            ErmseAggregateCalc tmp = new ErmseAggregateCalc();
            aggregateCalcList.add(tmp);
            /* 提取数据 */
            LocalDateTime tmpDateTime = timeBase.getDateTime();
            String timeK = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(tmpDateTime);
            // 时间
            tmp.setEventTime(tmpDateTime);
            // 容量
            Optional.ofNullable(capacityMap.get(timeK)).ifPresent(p -> {
                tmp.setCap(p.getPowerCalcCapacity());
                tmp.setCapForAssess(p.getCheckCalcCapacity());
            });
            // 短期功率数据
            Optional.of(cdqGorup.get(timeK)).ifPresent(l -> {
                List<BigDecimal> tmpList = l.stream().filter(Objects::nonNull).map(WfDataCdq::getForecastProduce).collect(Collectors.toList());
                tmp.setDqProduce(tmpList);
            });
            // 短期功率数据-单条
            Optional.ofNullable(cdqMap.get(timeK)).ifPresent(p -> {
                tmp.setForeset(p.getForecastProduce());
            });
            // 真实功率数据
            Optional.of(zrGroup.get(timeK)).ifPresent(l -> {
                List<BigDecimal> tmpList = l.stream().filter(Objects::nonNull).map(WfDataZr::getActualProduce).collect(Collectors.toList());
                tmp.setZrCapsProduce(tmpList);
            });
        }

        /*
            计算
         */
        // 过滤数据
        List<ErmseAggregateCalc> aggrs = aggregateCalcList.stream().filter(Objects::nonNull)
                .filter(item ->
                        Objects.nonNull(item.getCap())
                                && Objects.nonNull(item.getEventTime())
                                && CollectionUtils.isNotEmpty(item.getDqProduce())
                                && CollectionUtils.isNotEmpty(item.getZrCapsProduce()))
                .filter(item -> new BigDecimal("0").compareTo(item.getCap()) != 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(aggrs)) {
            log.warn("过滤数据后无计算数据可用");
            return;
        }
        // 不可变List
        aggrs = Collections.unmodifiableList(aggrs);

        /*   计算短期analysis数据    */

        //# 算法-计算Ermse[均方根误差]
        final BigDecimal fnRes = DqCalcUtils.calcErmse(aggrs);
        //# 算法-计算Ermse[均方根误差]

        //# 算法-计算EMAE[平均绝对误差]
        final BigDecimal emae = DqCalcUtils.calcEMAE(aggrs);
        //# 算法-计算EMAE[平均绝对误差]

        //# 最大预测误差 max(pmk - ppk)
        final BigDecimal maxe = DqCalcUtils.calcMaxe(aggrs);
        //# 最大预测误差

        //# 相关系数R
        final BigDecimal aboutR = DqCalcUtils.calcAboutR(aggrs);
        //# 相关系数R

        //# 准确率r1
        BigDecimal r1 = new BigDecimal("0");
        // (1 - Ermse) * 100%
        r1 = new BigDecimal("1").subtract(fnRes, new MathContext(3, RoundingMode.HALF_EVEN)).multiply(new BigDecimal("100"));
        //# 准确率r1

        //# 合格率r2
        final BigDecimal ratioR2 = DqCalcUtils.calcAboutR2(aggrs);
        //# 合格率r2

        /*   计算短期analysis数据    */


        // 获取今日日期
        LocalDateTime bgDate = TimeProcessUtils.parseLocalDateTimeWithSecondPattern(bg);
        LocalDateTime dayBegin = LocalDateTime.of(bgDate.toLocalDate(), LocalTime.MIN);

        // 存储
        QueryWrapper<WfAnalyseCdq> analyseCdqQueryWrapper = new QueryWrapper<>();
        analyseCdqQueryWrapper.eq("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(dayBegin));
        WfAnalyseCdq wfAnalyseCdq = analyseCdqService.getBaseMapper().selectOne(analyseCdqQueryWrapper);
        if (Objects.nonNull(wfAnalyseCdq)) {
            wfAnalyseCdq.setAvgRmse(fnRes);
            wfAnalyseCdq.setAvgMae(emae);
            wfAnalyseCdq.setBiggestDiff(maxe);
            wfAnalyseCdq.setR1Ratio(r1);
            wfAnalyseCdq.setR2Ratio(ratioR2);
            wfAnalyseCdq.setAboutR(aboutR);
            analyseCdqService.getBaseMapper().updateById(wfAnalyseCdq);
        } else {
            WfAnalyseCdq nst = WfAnalyseCdq.builder()
                    .calcDate(dayBegin)
                    .avgRmse(fnRes)
                    .avgMae(emae)
                    .biggestDiff(maxe)
                    .r1Ratio(r1)
                    .r2Ratio(ratioR2)
                    .aboutR(aboutR)
                    .build();
            analyseCdqService.getBaseMapper().insert(nst);
        }


        /*   计算考核结果数据    */

        //# 漏报次数 -- 此处无需处理(次日处理昨日漏报次数)
        int hiatus = 0;
        //# 漏报次数 -- 此处无需处理(次日处理昨日漏报次数)

        //# 超短期功率预测准确率
        final BigDecimal cdqRatioR1 = AssessCalcUtils.calcAssessRatioR1(aggrs);
        System.out.println("超短期功率预测准确率:" + cdqRatioR1);
        //# 超短期功率预测准确率


        //# 超短期功率预测准确率考核电量
        // （85%-当日超短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1），单位MW
        BigDecimal cdqElectricR1 = AssessCalcUtils.calcCdqElectricR1(cdqRatioR1);
        //# 超短期功率预测准确率考核电量


        //# 超短期功率预测准确率考核费用
        // 【超短期功率预测准确率考核电量】*1000*0.4153元/kWh（1000为统一单位），单位元
        BigDecimal cdqPayR1 = AssessCalcUtils.calcCdqPayR1(cdqElectricR1);
        //# 超短期功率预测准确率考核费用

        /*   计算考核结果数据    */

        // 存储
        QueryWrapper<WfAssessDay> assessDayQueryWrapper = new QueryWrapper<>();
        assessDayQueryWrapper.eq("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(dayBegin));
        WfAssessDay wfAssessDay = assessDayService.getBaseMapper().selectOne(assessDayQueryWrapper);
        if (Objects.nonNull(wfAssessDay)) {
            wfAssessDay.setCdqHiatus(hiatus);
            wfAssessDay.setCdqRatio(cdqRatioR1);
            wfAssessDay.setCdqElectric(cdqElectricR1);
            wfAssessDay.setCdqPay(cdqPayR1);
            assessDayService.getBaseMapper().updateById(wfAssessDay);
        } else {
            WfAssessDay nst = WfAssessDay.builder()
                    .version(0)
                    .calcDate(dayBegin)
                    .cdqHiatus(hiatus)
                    .cdqRatio(cdqRatioR1)
                    .cdqElectric(cdqElectricR1)
                    .cdqPay(cdqPayR1)
                    .build();
            assessDayService.getBaseMapper().insert(nst);
        }



    }


}
