package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;


import com.zhikuntech.intellimonitor.fanscada.domain.mapper.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQueryList;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.GoldenIdQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.service.BackendToGoldenService;
import org.bouncycastle.asn1.x509.qualified.QCStatement;
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

    @Override
    public List<Integer> getGoldenIdByNumberAndId(GoldenIdQuery goldenIdQuery) {
        List<Integer> dataIds = goldenIdQuery.getDataIds();
        return backendToGoldenMapper.getGoldenIdByWindNumberAndId(dataIds);
    }

}
