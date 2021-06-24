package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.mapper.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailOneService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: intelligence-monitor
 * @Description:
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
@Service
public class FanDetailOneServiceImpl implements FanDetailOneService {

    @Autowired
    private BackendToGoldenMapper backend;
    @Autowired
    private GoldenUtil goldenUtil;

    @Override
    public BaseResponse<FanModelDataVO> getData(String number) {
        try {

            FanModelDataVO modelDataVO = new FanModelDataVO(
                    new WindWheelVO(), new GearCaseVO(), new GeneratorVO(), new WheelSpiderVO());
            FanModelDataVO modelData = InjectPropertiesUtil.injectByAnnotationCustomize(modelDataVO, number, backend, goldenUtil);

            return BaseResponse.success(modelData);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "请求失败");
        }
    }
}
