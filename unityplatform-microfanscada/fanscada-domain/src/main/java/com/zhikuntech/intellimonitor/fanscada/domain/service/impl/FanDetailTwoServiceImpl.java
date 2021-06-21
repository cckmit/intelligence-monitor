package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.InjectPropertiesUtil;
import com.zhikuntech.intellimonitor.fanscada.domain.mapper.BackendToGoldenMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.mapper.FsBasicParameterMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FsBasicParameterVO;
import com.zhikuntech.intellimonitor.fanscada.domain.service.FanDetailTwoService;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: DAI
 * @date: Created in 2021/6/15 17:54
 */
@Service
public class FanDetailTwoServiceImpl implements FanDetailTwoService {

    @Autowired
    private FsBasicParameterMapper fsBasicParameterMapper;
    @Autowired
    private BackendToGoldenMapper backend;


    @Override
    public BaseResponse<FanDetailDataVO> getData(String number) {
        try {
            UpsTelemetryVO ups = InjectPropertiesUtil.injectByAnnotationCustomize(new UpsTelemetryVO(), number, backend);
            DcScreenVO dc = InjectPropertiesUtil.injectByAnnotationCustomize(new DcScreenVO(), number, backend);
            UpsTelemetryStatusVO upsStatus = InjectPropertiesUtil.injectByAnnotationCustomize(new UpsTelemetryStatusVO(), number, backend);
            DcScreenStatusVO dcStatus = InjectPropertiesUtil.injectByAnnotationCustomize(new DcScreenStatusVO(), number, backend);

            FanDetailDataVO leftDataVO = new FanDetailDataVO();
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

    @Override
    public BaseResponse<FsBasicParameterVO> getFanParameterByNumber(String number) {
        QueryWrapper<FsBasicParameterVO> query = new QueryWrapper<>();
        query.eq("number", number);
        return BaseResponse.success(fsBasicParameterMapper.selectOne(query));
    }
}
