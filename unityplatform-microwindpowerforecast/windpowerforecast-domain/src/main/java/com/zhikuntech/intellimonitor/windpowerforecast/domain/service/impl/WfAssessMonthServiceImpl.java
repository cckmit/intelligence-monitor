package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessCurveDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessMonth;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAssessMonthMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthAssessQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthCurveQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import org.apache.commons.lang3.StringUtils;
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
        List<MonthAssessCurveDTO> result = new ArrayList<>();
        if (Objects.isNull(query)) {
            return result;
        }
        String listMod = query.getQueryMod();//查询模式[查询某一年(one)/查询范围(range)]
        String listYearPre = query.getQueryYearPre();//前年
        String listYearPost = query.getQueryYearPost();//后年 one时为空
        // 构建query
        QueryWrapper<WfAssessMonth> criteria = new QueryWrapper<>();
        if (StringUtils.equalsIgnoreCase(listMod, MonthAssessQuery.QUERY_ONE)) {//如果查询模式one
            // check
            if (StringUtils.isBlank(listYearPre)) {//如果前年为空
                return result;
            }
            String yearStr = listYearPre + "-01-01";//开始时间
            LocalDate curYear = DateProcessUtils.parseToLocalDate(yearStr);//开始时间
            LocalDate nextYear = curYear.plusYears(1);//结束时间 开始时间年份+1
            criteria.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(curYear));//大于等于开始时间
            criteria.lt("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(nextYear));//小于结束时间
        } else {//如果查询模式是 多年查询范围(range)
            // check
            if (StringUtils.isBlank(listYearPre) || StringUtils.isBlank(listYearPost)) {
                return result;
            }
            String yearStrPre = listYearPre + "-01-01";
            String yearStrPost = listYearPost + "-01-01";
            LocalDate preYear = DateProcessUtils.parseToLocalDate(yearStrPre);//开始时间
            LocalDate postYear = DateProcessUtils.parseToLocalDate(yearStrPost);//结束时间
            postYear = postYear.plusDays(1);
            criteria.ge("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(preYear));
            criteria.lt("calc_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(postYear));
        }
        int pageNumber = 2100000000;
        int pageSize = 2100000000;
        // 构建Page
        Page<WfAssessMonth> page = new Page<>();
        page.setCurrent(pageNumber);
        page.setSize(pageSize);

        ArrayList<WfAssessMonth> list=new ArrayList<>();
     //   List<WfAssessMonth>

        // 查询
        Page<WfAssessMonth> fromDb = getBaseMapper().selectPage(page, criteria);
        Optional.ofNullable(fromDb.getRecords()).ifPresent(l -> {
            // 转换结果
            List<MonthAssessListDTO> tmpCol = l.stream().filter(Objects::nonNull).map(WfAssessMonthServiceImpl::switchMonthRes).collect(Collectors.toList());
     //       pagerRes.replaceWithNewCol(tmpCol);
        });

        //return pagerRes;

        return result;
    }


}
