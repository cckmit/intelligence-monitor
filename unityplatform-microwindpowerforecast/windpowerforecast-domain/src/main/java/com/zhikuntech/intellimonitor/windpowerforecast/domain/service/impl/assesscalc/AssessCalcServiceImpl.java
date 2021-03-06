package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.assesscalc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.*;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess.IWfAssessChangeService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess.IWfAssessDayService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assess.IWfAssessMonthService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assesscalc.AssessCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCapacityService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCdqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataDqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 考核结果计算
 *
 * <p>
 *     次日计算昨天电量及漏报次数
 * </p>
 *
 * @author liukai
 * @date 2021/06/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssessCalcServiceImpl implements AssessCalcService {


    private final IWfAssessChangeService changeService;

    private final IWfAssessDayService assessDayService;

    private final IWfDataCdqService cdqService;

    private final IWfDataDqService dqService;

    private final IWfDataCapacityService capacityService;

    private final IWfAssessMonthService monthService;


    /**
     * 计算昨日漏报次数(短期/超短期)
     * <p>
     *     此处不需要关注预测电量的更改操作,
     *     因为更改操作需要在数据的次日凌晨2点之后才可以进行修改.
     *     比如 2021-05-01的日评估数据,
     *     需要在 2021-05-02的凌晨2点才可以进行
     * </p>
     *
     * @param bg 时间格式[yyyy-MM-dd]
     */
    @Override public void calcYesterdayAssess(String bg) {
        // 计算昨日漏报次数
        LocalDateTime yesterday = DateProcessUtils.parseToLocalDateTime(bg);
        String yesterdayQueryStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday);
        QueryWrapper<WfAssessDay> assessDayWrapper = new QueryWrapper<>();
        assessDayWrapper.eq("calc_date", yesterdayQueryStr);
        WfAssessDay wfAssessDay = assessDayService.getBaseMapper().selectOne(assessDayWrapper);
        if (Objects.isNull(wfAssessDay)) {
            log.warn("[{}]没有查询到对应的日评估数据", bg);
            return;
        }

        /*
            【短期功率预测漏报次数】：当天已报记做0，当天未报记做1，对应考核比例值为0.05%
         */
        QueryWrapper<WfDataDq> dqQueryWrapper = new QueryWrapper<>();
        dqQueryWrapper.eq("body_time", 1);
        dqQueryWrapper.gt("event_date_time", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday));
        dqQueryWrapper.le("event_date_time", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday.plusDays(1)));
        dqQueryWrapper.gt("header_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday));
        dqQueryWrapper.le("header_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday.plusDays(1)));
        List<WfDataDq> wfDataDqs = dqService.getBaseMapper().selectList(dqQueryWrapper);
        if (CollectionUtils.isEmpty(wfDataDqs)) {
            // 当天已报记做0，当天未报记做1
            wfAssessDay.setDqHiatus(1);
        }

        /*
            【超短期功率预测漏报次数】：每天需上报96次，全部已报记做0，每漏报1次记做1，对应考核比例值为0.0005%
         */
        QueryWrapper<WfDataCdq> cdqQueryWrapper = new QueryWrapper<>();
        cdqQueryWrapper.gt("event_date_time", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday));
        cdqQueryWrapper.le("event_date_time", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday.plusDays(1)));
        cdqQueryWrapper.gt("header_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday));
        cdqQueryWrapper.le("header_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday.plusDays(1)));
        List<WfDataCdq> dataCdqs = cdqService.getBaseMapper().selectList(cdqQueryWrapper);
        if (CollectionUtils.isEmpty(dataCdqs)) {
            wfAssessDay.setCdqHiatus(96);
        } else if (dataCdqs.size() < 96) {
            wfAssessDay.setCdqHiatus(96 - dataCdqs.size());
        }

        int i = assessDayService.getBaseMapper().updateById(wfAssessDay);
        assert i == 1;
    }


    /**
     * <p>
     *     修改日考核数据后, 如果是当月的数据则需要重新计算
     * </p>
     *
     * 上个月的日期
     * @param bg 上月日期
     */
    @Override public void calcDayAndMonthAssessElectric(final String bg) {
        // 计算昨日考核电量
        // yyyy-MM-01   上月一号日期
        LocalDateTime monthBg = DateProcessUtils.parseToLocalDateTime(bg);
        LocalDateTime monthNextBg = monthBg.plusMonths(1);
        String monthBgStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(monthBg);
        String monthNextBgStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(monthNextBg);


        /*
            【单日考核电量】=当日短期功率预测上报率考核电量+当日短期功率预测准确率考核电量+当日超短期功率预测上报率考核电量+当日超短期功率准确率考核电量
            i.短期功率预测上报率考核电量=考核比例值（单日未上报记0.05%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            ii.短期功率预测准确率考核电量=（80%-当日短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
            iii.超短期功率预测上报率考核电量=考核比例值（单日每次未上报记0.0005%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            iiii.超短期功率预测准确率考核电量=（85%-当日超短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
         */

        // 获取当月全场发电量
        final BigDecimal curPlatFormGen = capacityService.fetchGenElectricWithDate(monthBgStr, monthNextBgStr);
        if (Objects.isNull(curPlatFormGen) || new BigDecimal("0").compareTo(curPlatFormGen) == 0) {
            log.warn("[{}]月没有风场发电量, 无法计算", bg);
            return;
        }
        // 风场考核小时数
        final BigDecimal checkHours = new BigDecimal("0.2");
        // 技术管理系数
        final BigDecimal techManage = new BigDecimal("1");
        // 装机容量
        final BigDecimal assembleCap = new BigDecimal("252");

        // 获取上月所有(日考核)数据
        QueryWrapper<WfAssessDay> assessDayQueryWrapper = new QueryWrapper<>();
        assessDayQueryWrapper.gt("calc_date", monthBgStr);
        assessDayQueryWrapper.lt("calc_date", monthNextBgStr);
        List<WfAssessDay> wfAssessDays = assessDayService.getBaseMapper().selectList(assessDayQueryWrapper);
        if (CollectionUtils.isEmpty(wfAssessDays)) {
            log.warn("[{}]当月没有日考核数据, 无法计算考核电量", bg);
            return;
        }
        // 自动核算考核值 短期/超短期

        // 单日未上报记0.05%，全月最大值为1%     0.0005
        int dqsum = wfAssessDays.stream().mapToInt(WfAssessDay::getDqHiatus).sum();
        // 单日每次未上报记0.0005%，全月最大值为1% 0.000005
        int cdqsum = wfAssessDays.stream().mapToInt(WfAssessDay::getCdqHiatus).sum();

        // 短期考核
        BigDecimal dqCheckV = new BigDecimal(dqsum).multiply(new BigDecimal("0.0005")).setScale(3, RoundingMode.HALF_EVEN);
        if (new BigDecimal("0.01").compareTo(dqCheckV) < 0) {
            dqCheckV = new BigDecimal("0.01");
        }
        // 超短期考核
        BigDecimal cdqCheckV = new BigDecimal(cdqsum).multiply(new BigDecimal("0.000005")).setScale(3, RoundingMode.HALF_EVEN);
        if (new BigDecimal("0.01").compareTo(cdqCheckV) < 0) {
            cdqCheckV = new BigDecimal("0.01");
        }

        for (WfAssessDay wfAssessDay : wfAssessDays) {
            // 短期准确率
            BigDecimal dqRatio = wfAssessDay.getDqRatio();
            // 超短期准确率
            BigDecimal cdqRatio = wfAssessDay.getCdqRatio();

            //短期功率预测上报率考核电量=考核比例值（单日未上报记0.05%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            BigDecimal tmp1 = dqCheckV.multiply(curPlatFormGen).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);
            //短期功率预测准确率考核电量=（80%-当日短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
            BigDecimal tmp2 = new BigDecimal("0.8").subtract(dqRatio).multiply(assembleCap).multiply(checkHours).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);
            //超短期功率预测上报率考核电量=考核比例值（单日每次未上报记0.0005%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            BigDecimal tmp3 = cdqCheckV.multiply(curPlatFormGen).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);
            //超短期功率预测准确率考核电量=（85%-当日超短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
            BigDecimal tmp4 = new BigDecimal("0.85").subtract(cdqRatio).multiply(assembleCap).multiply(checkHours).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);

            BigDecimal innerDayCheck = tmp1.add(tmp2).add(tmp3).add(tmp4);
            wfAssessDay.setDayAssessElectric(innerDayCheck);
        }
        assessDayService.updateBatchById(wfAssessDays);


        // -----------------------------------------------------------------------------------------------------

        // 获取上月所有(修改后考核)数据
        QueryWrapper<WfAssessChange> changeQueryWrapper = new QueryWrapper<>();
        changeQueryWrapper.eq("newest", 0);
        changeQueryWrapper.gt("calc_date", monthBgStr);
        changeQueryWrapper.lt("calc_date", monthNextBgStr);
        List<WfAssessChange> changeList = changeService.getBaseMapper().selectList(changeQueryWrapper);
        if (CollectionUtils.isEmpty(changeList)) {
            log.warn("[{}]当月没有修改过的日考核数据, 无法计算修改后的考核电量", bg);
            return;
        }
        Set<Integer> filterSet = changeList.stream().map(WfAssessChange::getDayRefId).collect(Collectors.toSet());
        List<WfAssessDay> rawAssessDays = wfAssessDays.stream().filter(item -> !filterSet.contains(item.getId())).collect(Collectors.toList());
        // 修改后的考核电量计算

        // 单日未上报记0.05%，全月最大值为1%     0.0005
        dqsum = rawAssessDays.stream().mapToInt(WfAssessDay::getDqHiatus).sum();
        dqsum += changeList.stream().mapToInt(WfAssessChange::getDqHiatus).sum();
        // 单日每次未上报记0.0005%，全月最大值为1% 0.000005
        cdqsum = rawAssessDays.stream().mapToInt(WfAssessDay::getCdqHiatus).sum();
        cdqsum += changeList.stream().mapToInt(WfAssessChange::getCdqHiatus).sum();

        // 短期考核
        dqCheckV = new BigDecimal(dqsum).multiply(new BigDecimal("0.0005")).setScale(3, RoundingMode.HALF_EVEN);
        if (new BigDecimal("0.01").compareTo(dqCheckV) < 0) {
            dqCheckV = new BigDecimal("0.01");
        }
        // 超短期考核
        cdqCheckV = new BigDecimal(cdqsum).multiply(new BigDecimal("0.000005")).setScale(3, RoundingMode.HALF_EVEN);
        if (new BigDecimal("0.01").compareTo(cdqCheckV) < 0) {
            cdqCheckV = new BigDecimal("0.01");
        }

        for (WfAssessChange change : changeList) {
            // 短期准确率
            BigDecimal dqRatio = change.getDqRatio();
            // 超短期准确率
            BigDecimal cdqRatio = change.getCdqRatio();

            //短期功率预测上报率考核电量=考核比例值（单日未上报记0.05%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            BigDecimal tmp1 = dqCheckV.multiply(curPlatFormGen).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);
            //短期功率预测准确率考核电量=（80%-当日短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
            BigDecimal tmp2 = new BigDecimal("0.8").subtract(dqRatio).multiply(assembleCap).multiply(checkHours).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);
            //超短期功率预测上报率考核电量=考核比例值（单日每次未上报记0.0005%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            BigDecimal tmp3 = cdqCheckV.multiply(curPlatFormGen).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);
            //超短期功率预测准确率考核电量=（85%-当日超短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
            BigDecimal tmp4 = new BigDecimal("0.85").subtract(cdqRatio).multiply(assembleCap).multiply(checkHours).multiply(techManage).setScale(3, RoundingMode.HALF_EVEN);

            BigDecimal innerDayCheck = tmp1.add(tmp2).add(tmp3).add(tmp4);
            change.setDayAssessElectric(innerDayCheck);
        }
        changeService.updateBatchById(changeList);

        //# 计算月考核结果
        calcMonthCheckData(monthBg, wfAssessDays, changeList);
    }


    /**
     * 计算月考核数据
     * TODO 修改了上月的日考核数据应该触发该计算
     *
     * @param monthBg       月开始第一天
     * @param wfAssessDays  日考核数据
     * @param changeList    修改后的日考核数据
     */
    @Override public void calcMonthCheckData(LocalDateTime monthBg, List<WfAssessDay> wfAssessDays, List<WfAssessChange> changeList) {
    /*
        计算月考核数据
     */

        List<WfAssessDay> calcMonthRawAssessDays = wfAssessDays.stream().filter(item -> !changeList.stream().map(WfAssessChange::getDayRefId).collect(Collectors.toSet()).contains(item.getId())).collect(Collectors.toList());

        // 自动核算考核电量
        BigDecimal autoCheckElectric = wfAssessDays.stream().map(WfAssessDay::getDayAssessElectric).reduce(new BigDecimal("0"), BigDecimal::add);
        // 自动核算考核费用 -> [自动核算考核电量 *1000 * 0.4153元/kWh]
        BigDecimal autoCheckPay = autoCheckElectric.multiply(new BigDecimal("1000"))
                .multiply(new BigDecimal("0.4153")).setScale(3, RoundingMode.HALF_EVEN);
        // 最终修改后考核电量
        BigDecimal electricAfterChange = calcMonthRawAssessDays.stream().map(WfAssessDay::getDayAssessElectric).reduce(new BigDecimal("0"), BigDecimal::add);
        electricAfterChange = changeList.stream().map(WfAssessChange::getDayAssessElectric).reduce(electricAfterChange, BigDecimal::add);
        // 最终修改后考核费用
        BigDecimal payAfterChange = electricAfterChange.multiply(new BigDecimal("1000"))
                .multiply(new BigDecimal("0.4153")).setScale(3, RoundingMode.HALF_EVEN);


        QueryWrapper<WfAssessMonth> assessMonthQueryWrapper = new QueryWrapper<>();
        assessMonthQueryWrapper.eq("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(monthBg));
        WfAssessMonth fetchMonth = monthService.getBaseMapper().selectOne(assessMonthQueryWrapper);
        if (Objects.nonNull(fetchMonth)) {
            fetchMonth.setAutoElectric(autoCheckElectric);
            fetchMonth.setAutoPay(autoCheckPay);
            fetchMonth.setFnlElectric(electricAfterChange);
            fetchMonth.setFnlPay(payAfterChange);

            //尝试计算对比结果
            final BigDecimal scheduleElectric = fetchMonth.getScheduleElectric();
            final BigDecimal schedulePay = fetchMonth.getSchedulePay();
            if (Objects.nonNull(scheduleElectric)) {
                Optional.ofNullable(fetchMonth.getAutoElectric()).ifPresent(item -> {
                    fetchMonth.setContrastElectric(item.subtract(scheduleElectric));
                });
                Optional.of(fetchMonth.getFnlElectric()).ifPresent(item -> {
                    fetchMonth.setFnlContrastElectric(item.subtract(scheduleElectric));
                });
            }
            if (Objects.nonNull(schedulePay)) {
                Optional.ofNullable(fetchMonth.getAutoPay()).ifPresent(item -> {
                    fetchMonth.setContrastPay(item.subtract(schedulePay));
                });
                Optional.ofNullable(fetchMonth.getFnlPay()).ifPresent(item -> {
                    fetchMonth.setFnlContrastPay(item.subtract(schedulePay));
                });
            }

            fetchMonth.setUpdateTime(LocalDateTime.now());
            monthService.getBaseMapper().updateById(fetchMonth);
        } else {
            // 不存在数据, 那么也不存在调度考核结果(必须有调度考核结果才能计算对比结果)
            WfAssessMonth assessMonth = WfAssessMonth.builder()
                    .version(0)
                    .calcDate(monthBg)
                    .autoElectric(autoCheckElectric)
                    .autoPay(autoCheckPay)
                    .fnlElectric(electricAfterChange)
                    .fnlPay(payAfterChange)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            monthService.getBaseMapper().insert(assessMonth);
        }
    }

    /**
     * 上个月的日期
     * @param bg 上月日期
     */
    @Deprecated
    public void calcdayAndMonthAssessElectricTmp(String bg) {
        // 计算昨日考核电量
        // yyyy-MM-dd   昨日日期

        /*
            【单日考核电量】=当日短期功率预测上报率考核电量+当日短期功率预测准确率考核电量+当日超短期功率预测上报率考核电量+当日超短期功率准确率考核电量
            i.短期功率预测上报率考核电量=考核比例值（单日未上报记0.05%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            ii.短期功率预测准确率考核电量=（80%-当日短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
            iii.超短期功率预测上报率考核电量=考核比例值（单日每次未上报记0.0005%，全月最大值为1%）*当月全场发电量*技术管理系数（默认为1）
            iiii.超短期功率预测准确率考核电量=（85%-当日超短期功率预测准确率）*装机容量（252MW）*风电场考核小时数（默认0.2）*技术管理系数（默认为1）
         */


        LocalDateTime yesterday = DateProcessUtils.parseToLocalDateTime(bg);
        LocalDateTime today = yesterday.plusDays(1);
        String yesterdayStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(yesterday);
        String todayStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(today);

        // 获取昨日考核数据 (如果没有, 则无法计算)
        QueryWrapper<WfAssessDay> assessDayQueryWrapper = new QueryWrapper<>();
        assessDayQueryWrapper.eq("calc_date", yesterdayStr);
        WfAssessDay wfAssessDay = assessDayService.getBaseMapper().selectOne(assessDayQueryWrapper);
        if (Objects.isNull(wfAssessDay)) {
            // 昨日未生成考核数据, 无法计算
            log.warn("[{}]未生成考核数据, 无法计算", yesterday);
            return;
        } else {
            // TODO 校验更改状态 (查询change表是否有对应的数据)
        }
        BigDecimal dqRatio = wfAssessDay.getDqRatio();
        BigDecimal cdqRatio = wfAssessDay.getCdqRatio();
        if (Objects.isNull(dqRatio) || Objects.isNull(cdqRatio)) {
            // 昨日生成数据但是未生成准确率
            log.warn("[{}]昨日生成考核数据但是未生成准确率, 无法计算", yesterday);
            return;
        }



        // TODO 获取当月全场发电量
        final BigDecimal curMonthElectric = new BigDecimal("100");

        // TODO 获取短期数据
        QueryWrapper<WfDataDq> dqQueryWrapper = new QueryWrapper<>();
        dqQueryWrapper.gt("event_date_time", yesterday);
        dqQueryWrapper.le("event_date_time", yesterday);
        dqQueryWrapper.eq("body_time", 1);
        List<WfDataDq> dataCdqs = dqService.getBaseMapper().selectList(dqQueryWrapper);

        BigDecimal ratioDq = new BigDecimal("0.05");

        // TODO 获取短期数据
        QueryWrapper<WfDataCdq> cdqQueryWrapper = new QueryWrapper<>();
        cdqQueryWrapper.gt("event_date_time", yesterday);
        cdqQueryWrapper.le("event_date_time", yesterday);
        cdqQueryWrapper.eq("body_time", 1);
        List<WfDataCdq> dqList = cdqService.getBaseMapper().selectList(cdqQueryWrapper);

        BigDecimal ratioCdq = new BigDecimal("0.05");

        // TODO 短期功率预测上报率考核电量


        // TODO 短期功率预测准确率考核电量


        // TODO 超短期功率预测上报率考核电量


        // TODO 超短期功率预测准确率考核电量


        // TODO
        wfAssessDay.setCdqHiatus(0);
        wfAssessDay.setDqHiatus(0);
        // TODO
        wfAssessDay.setDayAssessElectric(new BigDecimal("10"));

        // 存储
        assessDayService.getBaseMapper().updateById(wfAssessDay);

    }


}
