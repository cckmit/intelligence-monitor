package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl.analysis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.AvgPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseDq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAnalyseDqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.analysis.IWfAnalyseDqService;
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
 * 短期功率分析 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@Service
public class WfAnalyseDqServiceImpl extends ServiceImpl<WfAnalyseDqMapper, WfAnalyseDq> implements IWfAnalyseDqService {
    /**
     * 短期功率分析 列表模式
     */
    @Override
    public DqListAggregateDTO dqPowerAnalysis(PowerAnalysisQuery query) {
        DqListAggregateDTO aggregateDTO = new DqListAggregateDTO();
        // 校验参数
        if (Objects.isNull(query)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (Objects.isNull(query.getQueryMode())) {
            throw new IllegalArgumentException("查询模式不能为空");
        }
        String queryMode = query.getQueryMode();

        String preStr = null;
        String postStr = null;
        if ("day".equalsIgnoreCase(queryMode)) {
            String dateStrPre = query.getDateStrPre();
            String dateStrPost = query.getDateStrPost();
            LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPre);
            LocalDateTime post = DateProcessUtils.parseToLocalDateTime(dateStrPost);
            if (pre == null || post == null) {
                return aggregateDTO;
            }
            preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(pre);
            postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(post.plusDays(1));
        } else {
            String dateStrPre = query.getDateStrPre();//传参
            String dateStrPreNew = dateStrPre + "-01";
            LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPreNew);//开始时间转成时间格式
            LocalDateTime lastDay = pre.with(TemporalAdjusters.lastDayOfMonth());//结束时间 查询日子的月份最后一天
            LocalDateTime first = pre.with(TemporalAdjusters.firstDayOfMonth());//开始时间
            if (lastDay == null) {
                return aggregateDTO;
            }
            LocalDate nowDay = LocalDate.now();//今天
            Month preMonth=pre.getMonth();//输入的日期的月份
            Month nowMonth=nowDay.getMonth();//本月
            preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(first);//开始时间转成字符串格式
            postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(lastDay.plusDays(1));//结束时间填一天
            // criteria
            int i =preMonth.compareTo(nowMonth);//比较输入的月份和本月
            if (i<0){//如果查的月份比本月小
                //查询结束就是那个月的最后一天
            }else {//如果查的是本月
                postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(nowDay.plusDays(1));//查询结束日期是今天
            }
        }

        // criteria
        QueryWrapper<WfAnalyseDq> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("calc_date", preStr);
        queryWrapper.le("calc_date", postStr);
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
        Page<WfAnalyseDq> page = new Page<>(pageNumber, pageSize);
        Page<WfAnalyseDq> wfAnalyseDqPage = getBaseMapper().selectPage(page, queryWrapper);
        List<WfAnalyseDq> records = wfAnalyseDqPage.getRecords();

        if (CollectionUtils.isNotEmpty(records)) {
            List<DqPowerAnalysisDTO> tmp = new ArrayList<>();
            for (WfAnalyseDq record : records) {
                if (Objects.isNull(record)) {
                    tmp.add(null);
                }
                if (Objects.nonNull(record)) {
                    DqPowerAnalysisDTO dtoTmp = new DqPowerAnalysisDTO();
                    BeanUtils.copyProperties(record, dtoTmp);
                    tmp.add(dtoTmp);
                }
            }

            Pager<DqPowerAnalysisDTO> resultPage = new Pager<>(0, tmp);
            aggregateDTO.setPager(resultPage);
            resultPage.setTotalCount((int) page.getTotal());
        }

        // 查询均值
        QueryWrapper<WfAnalyseDq> avgQuery = new QueryWrapper<>();
        avgQuery.gt("calc_date", preStr);
        avgQuery.le("calc_date", postStr);
        avgQuery.select("avg(avg_rmse) avg_rmse, avg(avg_mae) avg_mae, avg(biggest_diff) biggest_diff, avg(about_r) about_r, avg(r1_ratio) r1_ratio, avg(r2_ratio) r2_ratio");
        WfAnalyseDq wfAnalyseDq = getBaseMapper().selectOne(avgQuery);
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
     * 短期功率分析 曲线模式
     */
    @Override
    public List<DqPowerAnalysisDTO> DqPowerAnalysisCurve(PowerAnalysisQuery query) {
        if (Objects.isNull(query)) {
            return new ArrayList<>();
        }
        List<DqPowerAnalysisDTO> results=new ArrayList<>();
        switch (query.getQueryMode()){
            case "day":
                String dateStrPre = query.getDateStrPre();//查询条件--开始时间
                String dateStrPost = query.getDateStrPost();//查询条件--结束时间
                LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPre);//开始时间转成时间格式
                LocalDateTime post = DateProcessUtils.parseToLocalDateTime(dateStrPost);//结束时间转成时间格式
                if (pre == null || post == null) {//判断时间是不是空的
                    return results;
                }
                String preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(pre);//开始时间转成字符串格式
                String postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(post.plusDays(1));//结束时间填一天
                // criteria
                QueryWrapper<WfAnalyseDq> queryWrapper = new QueryWrapper<>();//查 短期功率分析
                queryWrapper.gt("calc_date", preStr);//大于开始时间
                queryWrapper.le("calc_date", postStr);//小于等于结束时间

                List<WfAnalyseDq> wfAnalysedqs = getBaseMapper().selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(wfAnalysedqs)) {//判断records不为空
                    List<DqPowerAnalysisDTO> tmp = new ArrayList<>();//创建存dqPowerAnalysisDTO的list集合叫tmp
                    for (WfAnalyseDq a : wfAnalysedqs) {//遍历wfAnalyseCdqs
                        if (Objects.isNull(a)) {//判断wfAnalysedqs的这个位置不为null
                            tmp.add(null);
                        }
                        if (Objects.nonNull(a)) {//如果不为null
                            DqPowerAnalysisDTO dtoTmp = new DqPowerAnalysisDTO();//创建dqPowerAnalysisDTO对象dtoTmp
                            BeanUtils.copyProperties(a, dtoTmp);//a转成dtoTmp类
                            tmp.add(dtoTmp);//dtoTmp存进tmp
                        }
                    }
                    results=tmp;
                }
                return results;
            case "month":
                String dateStrPre1 = query.getDateStrPre();//查询条件--开始时间
                String dateStrPreNew = dateStrPre1 + "-01";
                LocalDateTime pre1 = DateProcessUtils.parseToLocalDateTime(dateStrPreNew);//开始时间转成时间格式
                LocalDateTime lastDay = pre1.with(TemporalAdjusters.lastDayOfMonth());//结束时间 查询日子的月份最后一天
                LocalDateTime first = pre1.with(TemporalAdjusters.firstDayOfMonth());//开始时间
                if (lastDay == null) {//判断时间是不是空的
                    return results;
                }
                LocalDate nowDay = LocalDate.now();//今天
                Month preMonth=pre1.getMonth();//输入的日期的月份
                Month nowMonth=nowDay.getMonth();//本月
                String preStr1 = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(first);//开始时间转成字符串格式
                String postStr1 = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(lastDay.plusDays(1));//结束时间填一天
                // criteria
                int i =preMonth.compareTo(nowMonth);//比较输入的月份和本月
                if (i<0){//如果查的月份比本月小
                    //查询结束就是那个月的最后一天
                }else {//如果查的是本月
                    postStr1 = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(nowDay.plusDays(1));//查询结束日期是今天
                }
                QueryWrapper<WfAnalyseDq> queryWrapper1 = new QueryWrapper<>();//查 短期功率分析
                queryWrapper1.gt("calc_date", preStr1);//大于开始时间
                queryWrapper1.le("calc_date", postStr1);//小于等于结束时间
                List<WfAnalyseDq> wfAnalysedqs1 = getBaseMapper().selectList(queryWrapper1);
                if (CollectionUtils.isNotEmpty(wfAnalysedqs1)) {//判断records不为空
                    List<DqPowerAnalysisDTO> tmp = new ArrayList<>();//创建存dqPowerAnalysisDTO的list集合叫tmp
                    for (WfAnalyseDq a : wfAnalysedqs1) {//遍历wfAnalyseCdqs
                        if (Objects.isNull(a)) {//判断wfAnalysedqs的这个位置不为null
                            tmp.add(null);
                        }
                        if (Objects.nonNull(a)) {//如果不为null
                            DqPowerAnalysisDTO dtoTmp = new DqPowerAnalysisDTO();//创建dqPowerAnalysisDTO对象dtoTmp
                            BeanUtils.copyProperties(a, dtoTmp);//a转成dtoTmp类
                            tmp.add(dtoTmp);//dtoTmp存进tmp
                        }
                    }
                    results=tmp;
                }
                return results;
        }
        return null;
    }
}
