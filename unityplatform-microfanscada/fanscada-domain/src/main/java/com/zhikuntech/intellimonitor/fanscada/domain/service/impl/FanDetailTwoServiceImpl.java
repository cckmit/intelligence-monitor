package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailTwoService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.DcScreenVO;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanLeftDataVO;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.UpsTelemetryVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: DAI
 * @date: Created in 2021/6/15 17:54
 */
@Service
public class FanDetailTwoServiceImpl implements FanDetailTwoService {

    @Override
    public BaseResponse<FanLeftDataVO> getLeftData(Integer fanId) {
        GoldenUtil goldenUtil = new GoldenUtil();
        // todo 根据风机ID获取数据项ID
        int[] ids = new int[30];
        int start = 68;
        for (int i = 0; i < 30; i++) {
            ids[i] = i + start;
        }
        List<ValueData> list = null;
        try {
            list = goldenUtil.getSnapshots(ids);
        } catch (Exception e) {
            return BaseResponse.failure(ResultCode.PARAMETER_ERROR, "参数错误");
        }
        UpsTelemetryVO ups = new UpsTelemetryVO();
        DcScreenVO dc = new DcScreenVO();
        dc = InjectPropertiesUtil.injectByAnnotationCustomize(dc, list);
        ups = InjectPropertiesUtil.injectByAnnotationCustomize(ups, list);
        if (dc == null || ups == null) {
            return BaseResponse.failure(ResultCode.PARAMETER_ERROR, "参数错误");
        }
        FanLeftDataVO leftDataVO = new FanLeftDataVO();
        leftDataVO.setDc(dc);
        leftDataVO.setUps(ups);

        return BaseResponse.success(leftDataVO);
    }

}
