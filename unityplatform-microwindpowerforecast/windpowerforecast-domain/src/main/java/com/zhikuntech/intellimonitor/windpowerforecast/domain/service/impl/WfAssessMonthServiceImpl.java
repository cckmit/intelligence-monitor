package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.MonthAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessMonth;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAssessMonthMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.MonthAssessQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        Integer pageNumber = query.getPageNumber();
        Integer pageSize = query.getPageSize();

        Page<WfAssessMonth> page = new Page<>();
        page.setCurrent(pageNumber);
        page.setSize(pageSize);
        // 构建Page

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

        // 查询
        Page<WfAssessMonth> fromDb = getBaseMapper().selectPage(page, criteria);
        Optional.ofNullable(fromDb.getRecords()).ifPresent(l -> {
            // 转换结果
            List<MonthAssessListDTO> tmpCol = l.stream().filter(Objects::nonNull).map(item -> {
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
            }).collect(Collectors.toList());
            pagerRes.replaceWithNewCol(tmpCol);
        });

        return pagerRes;
    }



}
