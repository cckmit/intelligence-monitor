package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.dqcalc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.dqcalc.DqCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
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


    static Function<LocalDateTime, String> capacityFunction = item -> {
        if (Objects.isNull(item)) {
            return "nil";
        }
        return TimeProcessUtils.formatLocalDateTimeWithSecondPattern(item);
    };

    /**
     * 时间向后落点
     * 如:
     * 00:11 -> 00:15
     * 00:12 -> 00:15
     * 00:15 -> 00:15
     */
    static Function<LocalDateTime, String> timePostRangeProcess = item -> {
        if (Objects.nonNull(item)) {
            LocalDate date = item.toLocalDate();
            LocalTime time = item.toLocalTime();

            int minute = time.getMinute();
            //# 具体算法
            int tNum = minute / 15;
            int div = minute % 15;

            int willPlus = tNum * 15;
            if (div != 0) {
                willPlus = willPlus + 15;
            }
            if (minute == 0) {
                willPlus = 0;
            }
            //# 具体算法
            LocalDateTime calcDt = LocalDateTime.of(date, LocalTime.of(time.getHour(), 0, 0))
                    .plusMinutes(willPlus);
            return TimeProcessUtils.formatLocalDateTimeWithSecondPattern(calcDt);
        }
        return "nil";
    };

    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now();
        String s = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(now);
        System.out.println(s);
        String apply = timePostRangeProcess.apply(now);
        System.out.println(apply);
        System.out.println();
    }

    //# 1.计算均方根误差

    public void calcErmse(String bg, String end, String headerDate) {
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

        // 短期功率数据
        QueryWrapper<WfDataDq> dqQueryWrapper = new QueryWrapper<>();
        dqQueryWrapper.gt("event_date_time", bg);
        dqQueryWrapper.le("event_date_time", end);
        dqQueryWrapper.eq("header_date", headerDate);
        List<WfDataDq> dqList = dqService.getBaseMapper().selectList(dqQueryWrapper);
        if (CollectionUtils.isEmpty(dqList)) {
            log.warn("时间区间[{}]-[{}],头部时间[{}]无短期功率数据.", bg, end, headerDate);
            return;
        }
        Map<String, List<WfDataDq>> dqGorup = dqList.stream().collect(Collectors.groupingBy(p -> timePostRangeProcess.apply(p.getEventDateTime())));


        // 真实功率数据
        QueryWrapper<WfDataZr> zrQueryWrapper = new QueryWrapper<>();
        zrQueryWrapper.gt("event_date_time", bg);
        zrQueryWrapper.le("event_date_time", end);
        List<WfDataZr> zrList = zrService.getBaseMapper().selectList(zrQueryWrapper);
        if (CollectionUtils.isEmpty(zrList)) {
            log.warn("时间区间[{}]-[{}]无真实功率数据.", bg, end);
            return;
        }
        Map<String, List<WfDataZr>> zrGroup = zrList.stream().collect(Collectors.groupingBy(p -> timePostRangeProcess.apply(p.getEventDateTime())));


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
                .collect(Collectors.toMap(p -> capacityFunction.apply(p.getEventDateTime()), p -> p));

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
            });
            // 短期功率数据
            Optional.of(dqGorup.get(timeK)).ifPresent(l -> {
                List<BigDecimal> tmpList = l.stream().filter(Objects::nonNull).map(WfDataDq::getForecastProduce).collect(Collectors.toList());
                tmp.setDqProduce(tmpList);
            });
            // 真实功率数据
            Optional.of(zrGroup.get(timeK)).ifPresent(l -> {
                List<BigDecimal> tmpList = l.stream().filter(Objects::nonNull).map(WfDataZr::getActualProduce).collect(Collectors.toList());
                tmp.setDqProduce(tmpList);
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
        if (CollectionUtils.isNotEmpty(aggrs)) {
            log.warn("过滤数据后无计算数据可用");
            return;
        }



        //# 算法-计算Ermse[均方根误差]
        final BigDecimal fnRes = calcErmse(aggrs);
        //# 算法-计算Ermse[均方根误差]

        //# 算法-计算EMAE[平均绝对误差]
        final BigDecimal emae = calcEMAE(aggrs);
        //# 算法-计算EMAE[平均绝对误差]

        //# TODO 最大预测误差

        //# TODO 最大预测误差

        //# TODO 相关系数R

        //# TODO 相关系数R

        //# 准确率r1
        BigDecimal r1Ratio = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> dqProduces = aggr.getDqProduce();
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ck
            BigDecimal ck = aggr.getCap();
            // ppk
            BigDecimal ppk = new BigDecimal("0");
            for (BigDecimal dqProduce : dqProduces) {
                ppk = ppk.add(dqProduce);
            }
            ppk = ppk.divide(BigDecimal.valueOf(dqProduces.size()), 3, RoundingMode.HALF_UP);
            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }
            pmk = pmk.divide(BigDecimal.valueOf(zrCapsProduces.size()), 3, RoundingMode.HALF_UP);


        }
        //# 准确率r1
        BigDecimal r1 = new BigDecimal("0");
        // (1 - Ermse) * 100%
        r1 = new BigDecimal("1").subtract(fnRes, new MathContext(3, RoundingMode.HALF_EVEN)).multiply(new BigDecimal("100"));
        //# 准确率r1

        //# TODO 合格率r2

        //# TODO 合格率r2

        // 存储
        LocalDateTime bgDate = TimeProcessUtils.parseLocalDateTimeWithSecondPattern(bg);
        LocalDateTime dayBegin = LocalDateTime.of(bgDate.toLocalDate(), LocalTime.MIN);
        QueryWrapper<WfAnalyseDq> analyseDqQueryWrapper = new QueryWrapper<>();
        analyseDqQueryWrapper.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(dayBegin));
        WfAnalyseDq analyseDq = analyseDqService.getBaseMapper().selectOne(analyseDqQueryWrapper);
        if (Objects.nonNull(analyseDq)) {
            analyseDq.setAvgRmse(fnRes);
            analyseDq.setAvgMae(emae);
            analyseDqService.getBaseMapper().updateById(analyseDq);
        } else {
            // todo
            WfAnalyseDq nst = WfAnalyseDq.builder()
                    .calcDate(dayBegin)
                    .avgMae(fnRes)
                    .avgMae(emae)
                    .build();
            analyseDqService.getBaseMapper().insert(nst);
        }
    }

    private static BigDecimal calcEMAE(List<ErmseAggregateCalc> aggrs) {
        BigDecimal emae = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> dqProduces = aggr.getDqProduce();
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ck
            BigDecimal ck = aggr.getCap();
            // ppk
            BigDecimal ppk = new BigDecimal("0");
            for (BigDecimal dqProduce : dqProduces) {
                ppk = ppk.add(dqProduce);
            }
            ppk = ppk.divide(BigDecimal.valueOf(dqProduces.size()), 3, RoundingMode.HALF_UP);
            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }
            pmk = pmk.divide(BigDecimal.valueOf(zrCapsProduces.size()), 3, RoundingMode.HALF_UP);

            // |pmk - ppk| / ck
            BigDecimal kkk = pmk.subtract(ppk).abs().divide(ck, 3, RoundingMode.HALF_EVEN);
            emae = emae.add(kkk);
        }
        // r / n
        emae = emae.divide(new BigDecimal(aggrs.size()), 3, RoundingMode.HALF_EVEN);
        return emae;
    }

    private static BigDecimal calcErmse(List<ErmseAggregateCalc> aggrs) {
        BigDecimal allAggre = new BigDecimal("0");
        for (ErmseAggregateCalc aggr : aggrs) {
            List<BigDecimal> dqProduces = aggr.getDqProduce();
            List<BigDecimal> zrCapsProduces = aggr.getZrCapsProduce();

            // ck
            BigDecimal ck = aggr.getCap();
            // ppk
            BigDecimal ppk = new BigDecimal("0");
            for (BigDecimal dqProduce : dqProduces) {
                ppk = ppk.add(dqProduce);
            }
            ppk = ppk.divide(BigDecimal.valueOf(dqProduces.size()), 3, RoundingMode.HALF_UP);
            // pmk
            BigDecimal pmk = new BigDecimal("0");
            for (BigDecimal zrCapsProduce : zrCapsProduces) {
                pmk = pmk.add(zrCapsProduce);
            }
            pmk = pmk.divide(BigDecimal.valueOf(zrCapsProduces.size()), 3, RoundingMode.HALF_UP);

            // [(pmk - ppk) / ck] ^ 2
            BigDecimal kkk = pmk.subtract(ppk).divide(ck, 3, RoundingMode.HALF_EVEN)
                    .pow(2);
            allAggre = allAggre.add(kkk);
        }
        // sqrt(r/n)
        double v = allAggre.divide(new BigDecimal(aggrs.size()), 3, RoundingMode.HALF_EVEN).doubleValue();
        v = Math.sqrt(v);
        return new BigDecimal(v, new MathContext(3, RoundingMode.HALF_EVEN));
    }


    @Data
    static class ErmseAggregateCalc {

        LocalDateTime eventTime;

        List<BigDecimal> dqProduce;

        List<BigDecimal> zrCapsProduce;

        BigDecimal cap;

        public ErmseAggregateCalc() {
            // 初始化数组
            this.dqProduce = new ArrayList<>();
            this.zrCapsProduce = new ArrayList<>();
        }
    }


    //# 2.计算平均绝对误差



    //# 3.计算最大预测误差    -》 【预测功率】-【实际功率】



    //# 4.计算相关系数



    //# 5.计算准确率



    //# 6.计算合格率




}
