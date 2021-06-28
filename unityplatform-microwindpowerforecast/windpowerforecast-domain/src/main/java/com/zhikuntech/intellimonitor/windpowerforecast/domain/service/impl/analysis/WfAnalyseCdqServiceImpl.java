package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.analysis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.AvgPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseCdq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAnalyseCdqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.analysis.IWfAnalyseCdqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 超短期功率分析 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@Service
public class WfAnalyseCdqServiceImpl extends ServiceImpl<WfAnalyseCdqMapper, WfAnalyseCdq> implements IWfAnalyseCdqService {
    /**
     * 超短期功率分析 列表模式
     */
    @Override
    public CdqListAggregateDTO cdqPowerAnalysis(PowerAnalysisQuery query) {
        CdqListAggregateDTO aggregateDTO = new CdqListAggregateDTO();
        // 校验参数
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (Objects.isNull(query.getQueryMode())) {
            throw new IllegalArgumentException("查询模式不能为空");
        }

        String dateStrPre = query.getDateStrPre();
        String dateStrPost = query.getDateStrPost();
        LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPre);
        LocalDateTime post = DateProcessUtils.parseToLocalDateTime(dateStrPost);
        if (pre == null || post == null) {
            return aggregateDTO;
        }
        String preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(pre);
        String postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(post.plusDays(1));
        String queryMode=query.getQueryMode();
        if (queryMode.equals("day")){
            postStr=postStr;
        }else if (queryMode.equals("month")){
            LocalDateTime first=pre.with(TemporalAdjusters.firstDayOfMonth());//月第一天
            LocalDateTime last=pre.with(TemporalAdjusters.lastDayOfMonth());//月最后一天
            if (first == null || last == null) {
                return aggregateDTO;
            }
            LocalDate nowDay = LocalDate.now();//今天
            Month preMonth=pre.getMonth();//输入的日期的月份
            Month nowMonth=nowDay.getMonth();//本月
            preStr =TimeProcessUtils.formatLocalDateTimeWithSecondPattern(first);
            postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(last.plusDays(1));
            int i =preMonth.compareTo(nowMonth);//比较输入的月份和本月
            if (i<0){//如果查的月份比本月小
                postStr = postStr;//查询结束就是那个月的最后一天
            }else {//如果查的是本月
                postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(nowDay.plusDays(1));//查询结束日期是今天
            }
        }else {
            return aggregateDTO;
        }
        // criteria
        QueryWrapper<WfAnalyseCdq> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("calc_date", preStr);//大于
        queryWrapper.le("calc_date", postStr);//小于等于
        queryWrapper.ge("newest", 0);
        // calcDate -> calc_date
        if ("calcDate".equalsIgnoreCase(query.getOderByField())) {
            if ("up".equalsIgnoreCase(query.getUpOrDown())) {
                queryWrapper.orderByAsc("calc_date");
            } else {
                queryWrapper.orderByDesc("calc_date");
            }
        }
        // 分页查询
        Integer pageNumber = query.getPageNumber();
        Integer pageSize = query.getPageSize();
        Page<WfAnalyseCdq> page = new Page<>(pageNumber, pageSize);
        Page<WfAnalyseCdq> wfAnalyseDqPage = getBaseMapper().selectPage(page, queryWrapper);
        List<WfAnalyseCdq> records = wfAnalyseDqPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<CdqPowerAnalysisDTO> tmp = new ArrayList<>();
            for (WfAnalyseCdq record : records) {
                if (Objects.isNull(record)) {
                    tmp.add(null);
                }
                if (Objects.nonNull(record)) {
                    CdqPowerAnalysisDTO dtoTmp = new CdqPowerAnalysisDTO();
                    BeanUtils.copyProperties(record, dtoTmp);
                    tmp.add(dtoTmp);
                }
            }
            Pager<CdqPowerAnalysisDTO> resultPage = new Pager<>(0, tmp);
            aggregateDTO.setPager(resultPage);
            resultPage.setTotalCount((int) page.getTotal());
        }
        QueryWrapper<WfAnalyseCdq> avgQuery = new QueryWrapper<>();
        avgQuery.gt("calc_date", preStr);
        avgQuery.le("calc_date", postStr);
        avgQuery.ge("newest", 0);
        avgQuery.select("avg(avg_rmse) avg_rmse, avg(avg_mae) avg_mae, avg(biggest_diff) biggest_diff, avg(about_r) about_r, avg(r1_ratio) r1_ratio, avg(r2_ratio) r2_ratio");
        WfAnalyseCdq wfAnalyseDq = getBaseMapper().selectOne(avgQuery);
        if (Objects.nonNull(wfAnalyseDq)) {
            AvgPowerAnalysisDTO avgPowerAnalysisDTO = AvgPowerAnalysisDTO.builder()
                    .avgRmseAvg(wfAnalyseDq.getAvgRmse())
                    .avgMaeAvg(wfAnalyseDq.getAvgMae())
                    .biggestDiffAvg(wfAnalyseDq.getBiggestDiff())
                    .aboutRAvg(wfAnalyseDq.getAboutR())
                    .r1RatioAvg(wfAnalyseDq.getR1Ratio())
                    .r2RatioAvg(wfAnalyseDq.getR2Ratio())
                    .build();
            aggregateDTO.setAvgAna(avgPowerAnalysisDTO);
        }
        return aggregateDTO;
    }

    /**
     * 超短期功率分析 曲线模式
     */
    @Override
    public List<CdqPowerAnalysisDTO> cdqPowerAnalysisCurve(PowerAnalysisQuery query) {
        if (Objects.isNull(query)) {
            return new ArrayList<>();
        }
        List<CdqPowerAnalysisDTO> results = new ArrayList<>();
        String dateStrPre = query.getDateStrPre();//查询条件--开始时间

        String dateStrPost = query.getDateStrPost();//查询条件--结束时间

        LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPre);//开始时间转成时间格式

        LocalDateTime post = DateProcessUtils.parseToLocalDateTime(dateStrPost);//结束时间转成时间格式

        if (pre == null || post == null) {//判断时间是不是空的
            return results;
        }
        String preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(pre);//开始时间转成字符串格式

        String postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(post.plusDays(1));//结束时间填一天
        String queryMode=query.getQueryMode();
        if (queryMode.equals("day")){
            postStr=postStr;
        }else if (queryMode.equals("month")){
            LocalDateTime first=pre.with(TemporalAdjusters.firstDayOfMonth());//月第一天
            LocalDateTime last=pre.with(TemporalAdjusters.lastDayOfMonth());//月最后一天
            if (first == null || last == null) {
                return results;
            }
            LocalDate nowDay = LocalDate.now();//今天
            Month preMonth=pre.getMonth();//输入的日期的月份
            Month nowMonth=nowDay.getMonth();//本月
            preStr =TimeProcessUtils.formatLocalDateTimeWithSecondPattern(first);
            postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(last.plusDays(1));
            int i =preMonth.compareTo(nowMonth);//比较输入的月份和本月
            if (i<0){//如果查的月份比本月小
                postStr = postStr;//查询结束就是那个月的最后一天
            }else {//如果查的是本月
                postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(nowDay.plusDays(1));//查询结束日期是今天
            }
        }else {
            return results;
        }
        // criteria
        QueryWrapper<WfAnalyseCdq> queryWrapper = new QueryWrapper<>();//查 超短期功率分析

        queryWrapper.gt("calc_date", preStr);//大于开始时间

        queryWrapper.le("calc_date", postStr);//小于等于结束时间

        queryWrapper.ge("newest", 0);//当日最新日期（0是 1否）大于等于0

        List<WfAnalyseCdq> wfAnalyseCdqs = getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(wfAnalyseCdqs)) {//判断records不为空
            List<CdqPowerAnalysisDTO> tmp = new ArrayList<>();//创建存CdqPowerAnalysisDTO的list集合叫tmp
            for (WfAnalyseCdq a : wfAnalyseCdqs) {//遍历wfAnalyseCdqs
                if (Objects.isNull(a)) {//判断wfAnalyseCdqs的这个位置不为null
                    tmp.add(null);
                }
                if (Objects.nonNull(a)) {//如果不为null
                    CdqPowerAnalysisDTO dtoTmp = new CdqPowerAnalysisDTO();//创建CdqPowerAnalysisDTO对象dtoTmp
                    BeanUtils.copyProperties(a, dtoTmp);//a转成dtoTmp类
                    tmp.add(dtoTmp);//dtoTmp存进tmp
                }
            }
            results = tmp;
        }
        return results;
    }

}