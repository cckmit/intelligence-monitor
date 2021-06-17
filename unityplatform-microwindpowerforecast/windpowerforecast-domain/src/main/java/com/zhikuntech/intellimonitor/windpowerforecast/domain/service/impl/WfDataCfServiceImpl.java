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
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.CalcUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.calc.DirectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        String dateStr = query.getDateStr();
        queryWrapper.eq("high_level", high);
        queryWrapper.gt("event_date_time", dateStr + " 00:00:00");
        queryWrapper.le("event_date_time", dateStr + " 23:59:59");

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
        pager.setTotalPage(0);
        if (Objects.isNull(query)) {
            return pager;
        }
        QueryWrapper<WfDataCf> queryWrapper = new QueryWrapper<>();

        // criteria
        String queryMode = query.getQueryMode();
        String high = query.getHigh();
        String dateStr = query.getDateStr();

        queryWrapper.eq("high_level", StringUtils.trim(high));
        queryWrapper.gt("event_date_time", dateStr + " 00:00:00");
        queryWrapper.le("event_date_time", dateStr + " 23:59:59");

        // page
        Page<WfDataCf> queryPage = new Page<>();
        queryPage.setCurrent(query.getPageNumber());
        queryPage.setSize(query.getPageSize());

        // fetch
        Page<WfDataCf> results = getBaseMapper().selectPage(queryPage, queryWrapper);

        // 数据转换
        List<WfDataCf> records = results.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<CfListDTO> dtoList = records.stream().filter(Objects::nonNull).map(item -> {
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
            pager.setList(dtoList);
        }

        pager.setTotalCount((int) results.getTotal());
        pager.setPageSize((int) results.getSize());
        pager.setTotalPage((int) results.getPages());


        return pager;
    }

}
