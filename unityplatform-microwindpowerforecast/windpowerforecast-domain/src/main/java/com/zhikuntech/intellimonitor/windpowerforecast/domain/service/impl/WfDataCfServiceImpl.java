package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfCurveDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.dto.normalusage.CfListDTO;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfDataCfMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfCurvePatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.query.normalusage.CfListPatternQuery;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataCfService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DateProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.CalcUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.DirectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 实测气象 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-15
 */
@Service
public class WfDataCfServiceImpl extends ServiceImpl<WfDataCfMapper, WfDataCf> implements IWfDataCfService {


    public static final BigDecimal ZERO_DECIMAL = new BigDecimal("0");
    public static final BigDecimal DECIMAL_100 = new BigDecimal("100");

    @Override
    public List<BigDecimal> queryHigh() {
        QueryWrapper<WfDataCf> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT high_level").orderByAsc("high_level");
        List<WfDataCf> wfDataCfs = getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(wfDataCfs)) {
            return new ArrayList<>();
        }
        return wfDataCfs.stream()
                .filter(Objects::nonNull).map(WfDataCf::getHighLevel)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<CfCurveDTO> cfCurveQuery(CfCurvePatternQuery query) {
        // TODO params check
        if (Objects.isNull(query)) {
            return new ArrayList<>();
        }

        // 查询
        QueryWrapper<WfDataCf> queryWrapper = new QueryWrapper<>();
        String queryMode = query.getQueryMode();
        String high = query.getHigh();

        String dateStrPre = query.getDateStrPre();
        String dateStrPost = query.getDateStrPost();

        LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPre);
        LocalDateTime post = DateProcessUtils.parseToLocalDateTime(dateStrPost);
        if (pre == null || post == null) {
            return new ArrayList<>();
        }

        String preStr=null;
        String postStr=null;
        if (queryMode.equals("day")){
            preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(pre);
            postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(post.plusDays(1));
        }else if(queryMode.equals("month")){
            LocalDateTime first=pre.with(TemporalAdjusters.firstDayOfMonth());
            LocalDateTime last=pre.with(TemporalAdjusters.lastDayOfMonth());
            if (first == null || last == null) {
                return new ArrayList<>();
            }
            preStr =TimeProcessUtils.formatLocalDateTimeWithSecondPattern(first);
            postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(last);
        }else {
            return new ArrayList<>();
        }


        queryWrapper.eq("high_level", high);
        queryWrapper.gt("event_date_time", preStr);
        queryWrapper.le("event_date_time", postStr);

        List<WfDataCf> wfDataCfs = getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(wfDataCfs)) {
            return new ArrayList<>();
        }

        // 收集方位
        final HashMap<String, List<WfDataCf>> container = new HashMap<>(16);

        wfDataCfs.stream()
                .filter(Objects::nonNull)
                .forEach(item -> {
                    BigDecimal windDirection = item.getWindDirection();
                    String drc = DirectionUtils.locateDirection(windDirection);
                    if (Objects.isNull(drc)) {
                        return;
                    }
                    // 判断方向
                    List<WfDataCf> tmp = container.get(drc);
                    if (CollectionUtils.isEmpty(tmp)) {
                        tmp = new ArrayList<>();
                        container.put(drc, tmp);
                    }
                    tmp.add(item);
                });

        final List<CfCurveDTO> results = new ArrayList<>();

        Collection<List<WfDataCf>> values = container.values();
        if (CollectionUtils.isEmpty(values)) {
            return results;
        }
        List<WfDataCf> allValueData = values.stream().filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(allValueData)) {
            return results;
        }
        final int allSize = allValueData.size();
        final BigDecimal decimalAllSize = BigDecimal.valueOf(allSize);
        BigDecimal allPower = allValueData.stream().filter(Objects::nonNull).map(WfDataCf::getCalcPower).filter(Objects::nonNull)
                .reduce(ZERO_DECIMAL, BigDecimal::add);

        container.forEach((k, v) -> {
            // TODO CALC
            assert CollectionUtils.isNotEmpty(v);
            int num = v.size();
            BigDecimal rangePower = v.stream().filter(Objects::nonNull).map(WfDataCf::getCalcPower).filter(Objects::nonNull)
                    .reduce(ZERO_DECIMAL, BigDecimal::add);
            BigDecimal numRatio = BigDecimal.valueOf(num).divide(decimalAllSize, 4, RoundingMode.HALF_UP)
                    .multiply(DECIMAL_100).setScale(2, RoundingMode.HALF_UP);

            BigDecimal powerRatio = null;
            if (allPower.compareTo(ZERO_DECIMAL) > 0) {
                powerRatio = rangePower.divide(allPower, 4, RoundingMode.HALF_UP)
                        .multiply(DECIMAL_100).setScale(2, RoundingMode.HALF_UP);
            }
            CfCurveDTO cfCurveDTO = CfCurveDTO.builder()
                    .directionName(k)
                    .num(num)
                    .numRatio(numRatio)
                    .calcPower(rangePower)
                    .calcPowerRatio(powerRatio)
                    .build();
            results.add(cfCurveDTO);
        });

        return results;
    }

    @Override
    public Pager<CfListDTO> cfListQuery(CfListPatternQuery query) {
        // TODO param check

        Pager<CfListDTO> pager = new Pager<>(0, new ArrayList<>());
        if (Objects.isNull(query)) {
            return pager;
        }
        QueryWrapper<WfDataCf> queryWrapper = new QueryWrapper<>();

        // criteria
        String queryMode = query.getQueryMode();
        String high = query.getHigh();

        String dateStrPre = query.getDateStrPre();
        String dateStrPost = query.getDateStrPost();
        LocalDateTime pre = DateProcessUtils.parseToLocalDateTime(dateStrPre);
        LocalDateTime post = DateProcessUtils.parseToLocalDateTime(dateStrPost);
        switch (queryMode){
            case "day":

                if (pre == null || post == null) {
                    return pager;
                }
                String preStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(pre);
                String postStr = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(post.plusDays(1));

                queryWrapper.eq("high_level", StringUtils.trim(high));
                queryWrapper.gt("event_date_time", preStr);
                queryWrapper.le("event_date_time", postStr);

                // page
                Page<WfDataCf> queryPage = new Page<>();
                queryPage.setCurrent(query.getPageNumber());
                queryPage.setSize(query.getPageSize());
                // fetch
                Page<WfDataCf> results = getBaseMapper().selectPage(queryPage, queryWrapper);
                // 数据转换
                List<WfDataCf> records = results.getRecords();
                List<CfListDTO> dtoList=zhuanhuan(records);
                pager.setList(dtoList);
                pager.setTotalCount((int) results.getTotal());
                return pager;
            case "month" :
                LocalDateTime first=pre.with(TemporalAdjusters.firstDayOfMonth());
                LocalDateTime last=pre.with(TemporalAdjusters.lastDayOfMonth());
                if (first == null || last == null) {
                    return pager;
                }
                String preStr1 = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(first);
                String postStr1 = TimeProcessUtils.formatLocalDateTimeWithSecondPattern(last);

                queryWrapper.eq("high_level", StringUtils.trim(high));
                queryWrapper.gt("event_date_time", preStr1);
                queryWrapper.le("event_date_time", postStr1);

                // page
                Page<WfDataCf> queryPage1 = new Page<>();
                queryPage1.setCurrent(query.getPageNumber());
                queryPage1.setSize(query.getPageSize());
                // fetch
                Page<WfDataCf> results1 = getBaseMapper().selectPage(queryPage1, queryWrapper);
                // 数据转换
                List<WfDataCf> records1 = results1.getRecords();
                List<CfListDTO> dtoList1=zhuanhuan(records1);
                pager.setList(dtoList1);
                pager.setTotalCount((int) results1.getTotal());
                return pager;
        }
        return null;
    }

    //数据转换 list
    public List<CfListDTO> zhuanhuan(List<WfDataCf> list){
        if (CollectionUtils.isNotEmpty(list)) {
            List<CfListDTO> dtoList = list.stream().filter(Objects::nonNull).map(item -> {
                BigDecimal pressure = item.getPressure();
                BigDecimal windSpeed = item.getWindSpeed();
                BigDecimal calcPower = CalcUtils.calcPower(pressure, windSpeed);
                CfListDTO cfListDTO = CfListDTO.builder()
                        .highLevel(item.getHighLevel())
                        .windDirection(item.getWindDirection())
                        .windSpeed(windSpeed)
                        .pressure(pressure)
                        .power(calcPower)
                        .build();
                return cfListDTO;
            }).collect(Collectors.toList());
            return dtoList;
        }
        return null;
    }

}
