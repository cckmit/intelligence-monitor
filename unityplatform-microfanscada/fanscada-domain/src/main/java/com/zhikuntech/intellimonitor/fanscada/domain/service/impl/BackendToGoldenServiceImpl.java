package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.fanscada.domain.mapper.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQueryList;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.GoldenIdQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/15 11:10
 * @Description mysql数据库和golden数据库关于点位的映射关系
 * @Version 1.0
 */
@Service
public class BackendToGoldenServiceImpl extends ServiceImpl<BackendToGoldenMapper,BackendToGolden> implements BackendToGoldenService {

    @Autowired
    private BackendToGoldenMapper backendToGoldenMapper;

    @Override
    public List<Integer> getGoldenIdByBackendIdOrNumber(BackendToGoldenQuery backendToGoldenQuery) {
        return backendToGoldenMapper.getGoldenIdByBackendIdOrNumber(backendToGoldenQuery);
    }

    @Override
    public List<Integer> listGoldenIdByBackendIdOrNumber(BackendToGoldenQueryList backendToGoldenQueryList) {
        return backendToGoldenMapper.listGoldenIdByBackendIdOrNumber(backendToGoldenQueryList);
    }

    @Override
    public int[] getGoldenIdByNumberAndId(GoldenIdQuery goldenIdQuery) {
        List<Integer> dataIds = goldenIdQuery.getDataIds();

        List<Integer> goldenIdByWindNumberAndId = backendToGoldenMapper.getGoldenIdByWindNumberAndId(dataIds);

        return goldenIdByWindNumberAndId.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public Integer getGoldenIdByNumberAndId(Integer fanNumber, int value) {

        LambdaQueryWrapper<BackendToGolden> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BackendToGolden::getNumber,fanNumber);
        queryWrapper.eq(BackendToGolden::getBackendId,value);
        BackendToGolden backendToGolden = backendToGoldenMapper.selectOne(queryWrapper);
        return backendToGolden.getGoldenId();
    }

    @Override
    public List<BackendToGolden> selectList(List<Integer> list) {

        return backendToGoldenMapper.getList(list);
    }

}
