package com.zhikuntech.intellimonitor.structuremonitor.domain.init;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.mapper.SedimentMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.mapper.StructureToGoldenMinMapper;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.SedimentData;
import com.zhikuntech.intellimonitor.structuremonitor.domain.pojo.StructureToGoldenMin;
import com.zhikuntech.intellimonitor.structuremonitor.domain.service.SedimentService;
import com.zhikuntech.intellimonitor.structuremonitor.domain.utils.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.structuremonitor.domain.vo.LiveSedimentationData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 滕楠
 * @className InitialValueStartUp
 * @create 2021/7/22 11:02
 **/
@Component
@Order(3)
public class InitialValueStartUp implements CommandLineRunner {

    @Resource
    private SedimentMapper sedimentMapper;

    @Resource
    private SedimentService service;

    @Resource
    private StructureToGoldenMinMapper structureToGoldenMinMapper;


    private final Integer fanSize = 63;
    @Override
    public void run(String... args) throws Exception {
        //沉降初始值获取
        QueryWrapper<SedimentData> wrapper = new QueryWrapper<>();
        List<SedimentData> sedimentData = sedimentMapper.selectList(wrapper);
        if (sedimentData.size() == fanSize) {
            return;
        }
        sedimentMapper.delete(wrapper);
        //
        LambdaQueryWrapper<StructureToGoldenMin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<StructureToGoldenMin> structureToGoldenMins = structureToGoldenMinMapper.selectList(lambdaQueryWrapper);
        int[] goldenIds = structureToGoldenMins.stream().mapToInt(StructureToGoldenMin::getGoldenId).toArray();

        List<ValueData> snapshots = GoldenUtil.getSnapshots(goldenIds);
        List<LiveSedimentationData> result = new ArrayList<>();
        for (int i = 1; i < 64; i++) {
            LiveSedimentationData liveSedimentationData = new LiveSedimentationData();
            liveSedimentationData.setFanNumber(i);
            result.add(liveSedimentationData);
        }
        List<LiveSedimentationData> resultList = InjectPropertiesUtil.injectByAnnotation(result,snapshots);

        List<SedimentData> sedimentDataList = new ArrayList<>();
        for (LiveSedimentationData liveSedimentationData : resultList) {
            SedimentData s = new SedimentData();
            s.setValue1(liveSedimentationData.getA1Subside().doubleValue());
            s.setValue2(liveSedimentationData.getA2Subside().doubleValue());
            s.setValue3(liveSedimentationData.getA3Subside().doubleValue());
            s.setValue4(liveSedimentationData.getA4Subside().doubleValue());
            s.setFanNumber(liveSedimentationData.getFanNumber());
            sedimentDataList.add(s);
        }
        service.saveBatch(sedimentDataList);
    }
}
