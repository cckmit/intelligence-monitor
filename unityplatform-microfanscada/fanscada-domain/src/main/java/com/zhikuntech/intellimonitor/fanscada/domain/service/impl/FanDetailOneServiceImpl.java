package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.core.commons.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.mapper.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailOneService;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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


    @Resource
    private FanDetailService fanDetailService;

    @Override
    public BaseResponse<FanModelDataVO> getData(String number) {
        try {

            FanModelDataVO modelDataVO = new FanModelDataVO(
                    new WindWheelVO(), new GearCaseVO(), new GeneratorVO(), new WheelSpiderVO());
            List<Integer> backendList = InjectPropertiesUtil.injectByAnnotationCustomize(modelDataVO);
            FanModelDataVO matchData = fanDetailService.getMatchData(modelDataVO, backendList, number);
            return BaseResponse.success(matchData);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "请求失败");
        }
    }
}
