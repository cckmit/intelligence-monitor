package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailOneService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.*;
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
    @Override
    public BaseResponse<FanModelDataVO> getData(String number) {

        GoldenUtil goldenUtil = new GoldenUtil();
        // todo 根据风机ID获取数据项ID
        int[] ids = new int[40];
        int start = 134;
        for (int i = 0; i < 40; i++) {
            ids[i] = i + start;
        }
        List<ValueData> list = null;
        try {
            list = goldenUtil.getSnapshots(ids);
            if (CollectionUtils.isEmpty(list)) {
                return BaseResponse.failure(ResultCode.DATD_NOT_EXCEPTION, "暂无数据");
            }
            WindWheelVO windWheelVO = InjectPropertiesUtil.injectByAnnotationCustomize(new WindWheelVO(), list);
            WheelSpiderVO wheelSpiderVO = InjectPropertiesUtil.injectByAnnotationCustomize(new WheelSpiderVO(), list);
            GearCaseVO gear = InjectPropertiesUtil.injectByAnnotationCustomize(new GearCaseVO(), list);
            GeneratorVO generatorVO = InjectPropertiesUtil.injectByAnnotationCustomize(new GeneratorVO(), list);

            FanModelDataVO modelDataVO = new FanModelDataVO();
            modelDataVO.setWindWheeldata(windWheelVO);
            modelDataVO.setGeneratordata(generatorVO);
            modelDataVO.setWheelSpiderdata(wheelSpiderVO);
            modelDataVO.setGeardata(gear);

            return BaseResponse.success(modelDataVO);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "请求失败");
        }
    }
}
