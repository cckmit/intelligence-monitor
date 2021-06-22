package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.assessresult.DayAssessListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAssessDay;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAssessDayMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.assessresult.DayAssessQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAssessDayService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 日考核结果 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-21
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WfAssessDayServiceImpl extends ServiceImpl<WfAssessDayMapper, WfAssessDay> implements IWfAssessDayService {

    private final WfAssessDayMapper dayMapper;


    @Override
    public Pager<DayAssessListDTO> queryDayList(DayAssessQuery query) {
        Pager<DayAssessListDTO> pagerRes = Pager.emptyPager();
        if (Objects.isNull(query)) {
            return pagerRes;
        }
        String date = query.getDate();
        Integer pageNumber = query.getPageNumber();
        Integer pageSize = query.getPageSize();

        // 参数校验
        if (StringUtils.isBlank(date)) {
            return pagerRes;
        }

        // 分页
        Page<DayAssessListDTO> pageCriteria = new Page<>();
        pageCriteria.setCurrent(pageNumber);
        pageCriteria.setSize(pageSize);
        // 条件
        String monthStr = date + "-01";
        LocalDate monthPre = DateProcessUtils.parseToLocalDate(monthStr);
        LocalDate monthPost = monthPre.plusMonths(1);

        String strMonthPre = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(monthPre);
        String strMonthPost = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(monthPost);

        // 查询
        List<DayAssessListDTO> results = dayMapper.dayListPattern(pageCriteria, strMonthPre, strMonthPost);
        if (CollectionUtils.isNotEmpty(results)) {
            pagerRes.replaceWithNewCol(results);
        }

        return pagerRes;
    }

}
