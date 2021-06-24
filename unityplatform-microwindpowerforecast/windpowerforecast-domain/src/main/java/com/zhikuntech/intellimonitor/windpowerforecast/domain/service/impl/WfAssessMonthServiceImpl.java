package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessCurveDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseDq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessMonth;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAssessMonthMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthAssessQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthCurveQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 月考核结果 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
@Service
public class WfAssessMonthServiceImpl extends ServiceImpl<WfAssessMonthMapper, WfAssessMonth> implements IWfAssessMonthService {


    @Override
    public Pager<MonthAssessListDTO> queryMonthList(MonthAssessQuery query) {
        Pager<MonthAssessListDTO> pagerRes = Pager.emptyPager();
        if (Objects.isNull(query)) {
            return pagerRes;
        }
        String queryMod = query.getQueryMod();
        String queryYearPre = query.getQueryYearPre();
        String queryYearPost = query.getQueryYearPost();

        // 构建query
        QueryWrapper<WfAssessMonth> criteria = new QueryWrapper<>();
        if (StringUtils.equalsIgnoreCase(queryMod, MonthAssessQuery.QUERY_ONE)) {
            // check
            if (StringUtils.isBlank(queryYearPre)) {
                return pagerRes;
            }
            String yearStr = queryYearPre + "-01-01";
            LocalDate curYear = DateProcessUtils.parseToLocalDate(yearStr);
            LocalDate nextYear = curYear.plusYears(1);
            criteria.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(curYear));
            criteria.lt("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(nextYear));
        } else {
            // check
            if (StringUtils.isBlank(queryYearPre) || StringUtils.isBlank(queryYearPost)) {
                return pagerRes;
            }
            String yearStrPre = queryYearPre + "-01-01";
            String yearStrPost = queryYearPost + "-01-01";
            LocalDate preYear = DateProcessUtils.parseToLocalDate(yearStrPre);
            LocalDate postYear = DateProcessUtils.parseToLocalDate(yearStrPost);
            postYear = postYear.plusDays(1);
            criteria.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(preYear));
            criteria.lt("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(postYear));
        }


        Integer pageNumber = query.getPageNumber();
        Integer pageSize = query.getPageSize();
        // 构建Page
        Page<WfAssessMonth> page = new Page<>();
        page.setCurrent(pageNumber);
        page.setSize(pageSize);

        // 查询
        Page<WfAssessMonth> fromDb = getBaseMapper().selectPage(page, criteria);
        Optional.ofNullable(fromDb.getRecords()).ifPresent(l -> {
            // 转换结果
            List<MonthAssessListDTO> tmpCol = l.stream().filter(Objects::nonNull).map(WfAssessMonthServiceImpl::switchMonthRes).collect(Collectors.toList());
            pagerRes.replaceWithNewCol(tmpCol);
        });

        return pagerRes;
    }

    private static MonthAssessListDTO switchMonthRes(WfAssessMonth item) {
        BigDecimal fnlContrastElectric = item.getFnlContrastElectric();
        BigDecimal fnlContrastPay = item.getFnlContrastPay();
        int fnlResult = 1;
        if (Objects.nonNull(fnlContrastElectric) || Objects.nonNull(fnlContrastPay)) {
            fnlResult = 0;
        }
        MonthAssessListDTO tmp = MonthAssessListDTO.builder()
                .calcDate(item.getCalcDate())
                .autoElectric(item.getAutoElectric())
                .autoPay(item.getAutoPay())
                .fnlElectric(item.getFnlElectric())
                .fnlPay(item.getFnlPay())
                .scheduleElectric(item.getScheduleElectric())
                .schedulePay(item.getSchedulePay())
                .contrastElectric(item.getContrastElectric())
                .contrastPay(item.getContrastPay())
                .fnlContrastElectric(fnlContrastElectric)
                .fnlContrastPay(fnlContrastPay)
                .fnlResult(fnlResult)
                .build();
        return tmp;
    }

    @Override
    public List<MonthAssessCurveDTO> queryMonthCurve(MonthCurveQuery query) {
        // TODO
        //list<显示数据>
        List<MonthAssessCurveDTO> result = new ArrayList<>();
        if (Objects.isNull(query)) {
            return result;
        }
        String queryMod = query.getQueryMod();//查询模式
        String queryYearPre = query.getQueryYearPre();//前年
        String queryYearPost = query.getQueryYearPost();//后年
        //构建query
        QueryWrapper<WfAssessMonth> criteria = new QueryWrapper<>();
        if (StringUtils.equalsIgnoreCase(queryMod, MonthAssessQuery.QUERY_ONE)) {//如果模式是one
            // check
            if (StringUtils.isBlank(queryYearPre)) {
                return result;
            }
            String yearStr = queryYearPre + "-01-01";//前年
            LocalDate curYear = DateProcessUtils.parseToLocalDate(yearStr);//开始时间
            LocalDate nextYear = curYear.plusYears(1);//结束时间
            criteria.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(curYear));//大于等于
            criteria.lt("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(nextYear));//小于
        } else {
            // check
            if (StringUtils.isBlank(queryYearPre) || StringUtils.isBlank(queryYearPost)) {
                return result;
            }
            String yearStrPre = queryYearPre + "-01-01";//开始时间
            String yearStrPost = queryYearPost + "-01-01";//结束时间
            LocalDate preYear = DateProcessUtils.parseToLocalDate(yearStrPre);//开始时间
            LocalDate postYear = DateProcessUtils.parseToLocalDate(yearStrPost);//结束时间
            postYear = postYear.plusDays(1);//结束时间加一
            criteria.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(preYear));//大于等于
            criteria.lt("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(postYear));//小于
        }
        // 查询
        List<WfAssessMonth> fromDb=getBaseMapper().selectList(criteria);
        //转换结果
        if (CollectionUtils.isNotEmpty(fromDb)) {//判断fromDb不为空
            List<MonthAssessCurveDTO> tmp = new ArrayList<>();//创建存MonthAssessCurveDTO的list集合叫tmp
            for (WfAssessMonth a : fromDb) {//遍历fromDb
                if (Objects.isNull(a)) {//判断fromDb的这个位置不为null
                    tmp.add(null);
                }
                if (Objects.nonNull(a)) {//如果a不为null
                    MonthAssessCurveDTO dtoTmp = new MonthAssessCurveDTO();//创建MonthAssessCurveDTO对象dtoTmp
                    BeanUtils.copyProperties(a, dtoTmp);//a转成dtoTmp类
                    tmp.add(dtoTmp);//dtoTmp存进tmp
                }
            }
            result=tmp;
            return result;
        }
        return result;
    }

}
