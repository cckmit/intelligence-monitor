package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.AvgPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqListAggregateDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.statisticsanalysis.DqPowerAnalysisDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfAnalyseDq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfAnalyseDqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.statisticsanalysis.PowerAnalysisQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfAnalyseDqService;
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
 * 短期功率分析 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
@Service
public class WfAnalyseDqServiceImpl extends ServiceImpl<WfAnalyseDqMapper, WfAnalyseDq> implements IWfAnalyseDqService {

    @Override
    public DqListAggregateDTO dqPowerAnalysis(PowerAnalysisQuery query) {
        DqListAggregateDTO aggregateDTO = new DqListAggregateDTO();
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
        QueryWrapper<WfAnalyseDq> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("calc_date", preStr);
        queryWrapper.le("calc_date", postStr);

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
            resultPage.setTotalPage((int) page.getPages());
            resultPage.setPageSize((int) page.getSize());
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
}
