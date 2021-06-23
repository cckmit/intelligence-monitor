package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.dqcalc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.dqcalc.DqCalcService;
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
 * 该实现关注数据的计算,不关注计算触发的时机.
 * <p>
 *     外部触发需要保证: 保证满足触发计算的条件
 * </p>
 *
 * @author liukai
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DqCalcServiceImpl implements DqCalcService {

    /**
     * 时间基准
     */
    private final IWfTimeBaseService timeBaseService;

    /**
     * 短期预测功率
     */
    private final IWfDataDqService dqService;

    /**
     * 真实功率
     */
    private final IWfDataZrService zrService;

    /**
     * 容量数据
     */
    private final IWfDataCapacityService capacityService;

    /**
     * 短期功率分析
     */
    private final IWfAnalyseDqService analyseDqService;

    /**
     * 日考核结果
     */
    private final IWfAssessDayService assessDayService;


    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now();
        String s = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(now);
        System.out.println(s);
        String apply = CalcCommonUtils.timePostRangeProcess.apply(now);
        System.out.println(apply);
        System.out.println();
    }

    //# 1.计算均方根误差

    @Override public void dqDataCalc(String bg, String end, String headerDate) {
        /*
            bg  ->  yyyy-MM-dd HH:mm:ss
            end ->  yyyy-MM-dd HH:mm:ss
            headerDate  ->  yyyy-MM-dd HH:mm:ss
         */

        /*
            获取数据
            查询 [bg - end)时间范围的数据
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

        // 短期功率数据 - 每15分钟一条数据
        QueryWrapper<WfDataDq> dqQueryWrapper = new QueryWrapper<>();
        dqQueryWrapper.gt("event_date_time", bg);
        dqQueryWrapper.le("event_date_time", end);
        dqQueryWrapper.eq("header_date", headerDate);
        List<WfDataDq> dqList = dqService.getBaseMapper().selectList(dqQueryWrapper);
        if (CollectionUtils.isEmpty(dqList)) {
            log.warn("时间区间[{}]-[{}],头部时间[{}]无短期功率数据.", bg, end, headerDate);
            return;
        }
        Map<String, List<WfDataDq>> dqGorup = dqList.stream().collect(Collectors.groupingBy(p -> CalcCommonUtils.timePostRangeProcess.apply(p.getEventDateTime())));

        Map<String, WfDataDq> dqMap = dqList.stream().collect(Collectors.toMap(p -> CalcCommonUtils.localDateTimeStringFunction.apply(p.getEventDateTime()), p -> p, (p1, p2) -> p2));


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
            Optional.of(dqGorup.get(timeK)).ifPresent(l -> {
                List<BigDecimal> tmpList = l.stream().filter(Objects::nonNull).map(WfDataDq::getForecastProduce).collect(Collectors.toList());
                tmp.setDqProduce(tmpList);
            });
            // 短期功率数据-单条
            Optional.ofNullable(dqMap.get(timeK)).ifPresent(p -> {
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

        // 获取今日日期
        LocalDateTime bgDate = TimeProcessUtils.parseLocalDateTimeWithSecondPattern(bg);
        LocalDateTime dayBegin = LocalDateTime.of(bgDate.toLocalDate(), LocalTime.MIN);

        // 存储
        QueryWrapper<WfAnalyseDq> analyseDqQueryWrapper = new QueryWrapper<>();
        analyseDqQueryWrapper.eq("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(dayBegin));
        WfAnalyseDq analyseDq = analyseDqService.getBaseMapper().selectOne(analyseDqQueryWrapper);
        if (Objects.nonNull(analyseDq)) {
            analyseDq.setAvgRmse(fnRes);
            analyseDq.setAvgMae(emae);
            analyseDq.setBiggestDiff(maxe);
            analyseDq.setR1Ratio(r1);
            analyseDq.setR2Ratio(ratioR2);
            analyseDq.setAboutR(aboutR);
            analyseDqService.getBaseMapper().updateById(analyseDq);
        } else {
            WfAnalyseDq nst = WfAnalyseDq.builder()
                    .calcDate(dayBegin)
                    .avgRmse(fnRes)
                    .avgMae(emae)
                    .biggestDiff(maxe)
                    .r1Ratio(r1)
                    .r2Ratio(ratioR2)
                    .aboutR(aboutR)
                    .build();
            analyseDqService.getBaseMapper().insert(nst);
        }

        /*   计算考核结果数据    */

        //# TODO 漏报次数
        int hiatus = 0;
        //# TODO 漏报次数

        //# 短期功率预测准确率
        final BigDecimal cdqRatioR1 = AssessCalcUtils.calcAssessRatioR1(aggrs);
        System.out.println("超短期功率预测准确率:" + cdqRatioR1);
        //# 短期功率预测准确率

        //# 短期功率预测准确率考核电量
        // （85%-当日短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1），单位MW
        BigDecimal cdqElectricR1 = new BigDecimal("0.80").subtract(cdqRatioR1)
                .multiply(new BigDecimal("252")).multiply(new BigDecimal("0.2"))
                .multiply(new BigDecimal("1")).setScale(3, RoundingMode.HALF_EVEN);
        //# 短期功率预测准确率考核电量


        //# 短期功率预测准确率考核费用
        // 【短期功率预测准确率考核电量】*1000*0.4153元/kWh（1000为统一单位），单位元
        BigDecimal cdqPayR1 = cdqElectricR1.multiply(new BigDecimal("1000"))
                .multiply(new BigDecimal("0.4153"))
                .setScale(3, RoundingMode.HALF_EVEN);
        //# 短期功率预测准确率考核费用

        /*   计算考核结果数据    */

        // 存储
        QueryWrapper<WfAssessDay> assessDayQueryWrapper = new QueryWrapper<>();
        assessDayQueryWrapper.eq("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(dayBegin));
        WfAssessDay wfAssessDay = assessDayService.getBaseMapper().selectOne(assessDayQueryWrapper);
        if (Objects.nonNull(wfAssessDay)) {
            wfAssessDay.setDqHiatus(hiatus);
            wfAssessDay.setDqRatio(cdqRatioR1);
            wfAssessDay.setDqElectric(cdqElectricR1);
            wfAssessDay.setDqPay(cdqPayR1);
            assessDayService.getBaseMapper().updateById(wfAssessDay);
        } else {
            WfAssessDay nst = WfAssessDay.builder()
                    .version(0)
                    .calcDate(dayBegin)
                    .dqHiatus(hiatus)
                    .dqRatio(cdqRatioR1)
                    .dqElectric(cdqElectricR1)
                    .dqPay(cdqPayR1)
                    .build();
            assessDayService.getBaseMapper().insert(nst);
        }

    }

}
