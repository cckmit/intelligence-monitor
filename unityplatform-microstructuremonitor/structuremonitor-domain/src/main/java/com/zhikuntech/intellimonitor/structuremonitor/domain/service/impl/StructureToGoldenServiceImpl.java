package com.zhikuntech.intellimonitor.structuremonitor.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.mapper.StructureToGoldenAvgMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.mapper.StructureToGoldenMaxMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.mapper.StructureToGoldenMinMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenAvg;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMax;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMin;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.StructureToGoldenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 滕楠
 * @className StructureToGoldenServiceImpl
 * @create 2021/7/19 10:55
 **/
@Service
public class StructureToGoldenServiceImpl implements StructureToGoldenService {

    @Resource
    private StructureToGoldenMinMapper minMapper;

    @Resource
    private StructureToGoldenMaxMapper maxMapper;

    @Resource
    private StructureToGoldenAvgMapper avgMapper;

    @Override
    public List<StructureToGoldenMin> getMinMap(Integer fanNumber, Integer dateType) {
        LambdaQueryWrapper<StructureToGoldenMin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StructureToGoldenMin::getFanNumber,fanNumber);
        wrapper.eq(StructureToGoldenMin::getDataType,dateType);
        return minMapper.selectList(wrapper);

    }

    @Override
    public List<StructureToGoldenMax> getMaxMap(Integer fanNumber, Integer dateType) {
        LambdaQueryWrapper<StructureToGoldenMax> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StructureToGoldenMax::getFanNumber,fanNumber);
        wrapper.eq(StructureToGoldenMax::getDataType,dateType);
        return maxMapper.selectList(wrapper);

    }

    @Override
    public List<StructureToGoldenAvg> getAvgMap(Integer fanNumber, Integer dateType) {
        LambdaQueryWrapper<StructureToGoldenAvg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StructureToGoldenAvg::getFanNumber,fanNumber);
        wrapper.eq(StructureToGoldenAvg::getDataType,dateType);
        return avgMapper.selectList(wrapper);

    }





    @Override
    public List<StructureToGoldenMin> getInitData(List<Integer> list) {


        return null;
    }
}