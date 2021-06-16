package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.rtdb.api.model.ValueData;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailTwoService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: DAI
 * @date: Created in 2021/6/15 17:54
 */
@Service
public class FanDetailTwoServiceImpl implements FanDetailTwoService {

    @Override
    public BaseResponse<FanLeftDataVO> getData(Integer fanId) {
        GoldenUtil goldenUtil = new GoldenUtil();
        // todo 根据风机ID获取数据项ID
        int[] ids = new int[61];
        int start = 68;
        for (int i = 0; i < 30; i++) {
            ids[i] = i + start;
        }
        start = 103;
        for (int i = 0; i < 31; i++) {
            int temp = 30 + i;
            ids[temp] = i + start;
        }


        List<ValueData> list = null;
        try {
            list = goldenUtil.getSnapshots(ids);
            if (CollectionUtils.isEmpty(list)) {
                return BaseResponse.failure(ResultCode.DATD_NOT_EXCEPTION, "暂无数据");
            }
            UpsTelemetryVO ups = InjectPropertiesUtil.injectByAnnotationCustomize(new UpsTelemetryVO(), list);
            DcScreenVO dc = InjectPropertiesUtil.injectByAnnotationCustomize(new DcScreenVO(), list);
            UpsTelemetryStatusVO upsStatus = InjectPropertiesUtil.injectByAnnotationCustomize(new UpsTelemetryStatusVO(), list);
            DcScreenStatusVO dcStatus = InjectPropertiesUtil.injectByAnnotationCustomize(new DcScreenStatusVO(), list);

            FanLeftDataVO leftDataVO = new FanLeftDataVO();
            leftDataVO.setDcData(dc);
            leftDataVO.setUpsData(ups);
            leftDataVO.setUpsStatus(upsStatus);
            leftDataVO.setDcStatus(dcStatus);
            return BaseResponse.success(leftDataVO);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.failure(ResultCode.REQUEST_ERROR, "请求失败");
        }
    }
}
