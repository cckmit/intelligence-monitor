package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.AvgPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.CdqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseCdq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAnalyseCdqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAnalyseCdqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public CdqListAggregateDTO cdqPowerAnalysis(PowerAnalysisQuery query) {
        CdqListAggregateDTO aggregateDTO = new CdqListAggregateDTO();
        if (Objects.isNull(query)) {
            return aggregateDTO;
        }
        String queryMode = query.getQueryMode();
        String dateStrPre = query.getDateStrPre();
        String dateStrPost = query.getDateStrPost();
        LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPre);
        LocalDateTime post = DateProcessUtils.parseToLocalDateTime(dateStrPost);
        if (pre == null || post == null) {
            return aggregateDTO;
        }
        String preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(pre);
        String postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(post.plusDays(1));
        // criteria
        QueryWrapper<WfAnalyseCdq> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("calc_date", preStr);
        queryWrapper.le("calc_date", postStr);
        queryWrapper.ge("newest", 0);


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
            resultPage.setTotalPage((int) page.getPages());
            resultPage.setPageSize((int) page.getSize());
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
}
