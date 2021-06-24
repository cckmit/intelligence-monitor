package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.assesscalc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessDay;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCdq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataDq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessDayService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataCdqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataDqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.assesscalc.AssessCalcService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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


    private final IWfAssessDayService assessDayService;

    private final IWfDataCdqService cdqService;

    private final IWfDataDqService dqService;


    public void calcYesterdayAssessElectric(String bg) {
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
