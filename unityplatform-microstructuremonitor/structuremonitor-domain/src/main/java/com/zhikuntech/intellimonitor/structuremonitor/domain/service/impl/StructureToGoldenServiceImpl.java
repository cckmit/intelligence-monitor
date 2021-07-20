package com.zhikuntech.intellimonitor.structuremonitor.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.mapper.StructureToGoldenMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGolden;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.StructureToGoldenService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 滕楠
 * @className StructureToGoldenServiceImpl
 * @create 2021/7/19 10:55
 **/
public class StructureToGoldenServiceImpl implements StructureToGoldenService {

    @Resource
    private StructureToGoldenMapper structureToGoldenMapper;

    @Override
    public List<StructureToGolden> getMap(Integer fanNumber) {
        LambdaQueryWrapper<StructureToGolden> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StructureToGolden::getFanNumber,fanNumber);
        List<StructureToGolden> structureToGoldens = structureToGoldenMapper.selectList(wrapper);
        return structureToGoldens;

    }

    @Override
    public List<StructureToGolden> getInitData(List<Integer> list) {


        return null;
    }
}