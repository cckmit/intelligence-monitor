package com.zhikuntech.intellimonitor.structuredata.domain.service.impl;

import com.zhikuntech.intellimonitor.structuredata.domain.entity.ShakeData;
import com.zhikuntech.intellimonitor.structuredata.domain.mapper.ShakeDataMapper;
import com.zhikuntech.intellimonitor.structuredata.domain.service.ShakeDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tn
 * @since 2021-07-15
 */
@Service
public class ShakeDataServiceImpl extends ServiceImpl<ShakeDataMapper, ShakeData> implements ShakeDataService {

    @Resource
    private ShakeDataMapper shakeDataMapper;

    @Override
    public ShakeData getFanData(String fanNumber) {
        String param = "国电普陀风电场_" + fanNumber + "号风机_塔筒振动";

        return shakeDataMapper.getFanData(param);

    }
}
