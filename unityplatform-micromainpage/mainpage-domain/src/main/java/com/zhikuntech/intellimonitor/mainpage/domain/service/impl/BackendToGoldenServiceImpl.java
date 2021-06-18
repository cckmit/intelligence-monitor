package com.zhikuntech.intellimonitor.mainpage.domain.service.impl;

import com.zhikuntech.intellimonitor.mainpage.domain.mapper.model.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGolden;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQueryList;
import com.zhikuntech.intellimonitor.mainpage.domain.service.BackendToGoldenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/15 11:10
 * @Description mysql数据库和golden数据库关于点位的映射关系
 * @Version 1.0
 */
@Service
public class BackendToGoldenServiceImpl implements BackendToGoldenService {
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
}
