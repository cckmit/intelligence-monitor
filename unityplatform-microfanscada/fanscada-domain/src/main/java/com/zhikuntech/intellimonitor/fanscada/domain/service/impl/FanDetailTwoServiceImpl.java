package com.zhikuntech.intellimonitor.fanscada.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.fanscada.domain.golden.GoldenUtil;
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
    @Autowired
    private GoldenUtil goldenUtil;


    @Override
    public BaseResponse<FanDetailDataVO> getData(String number) {
        try {

            FanDetailDataVO leftDataVO = new FanDetailDataVO(
                    new UpsTelemetryVO(), new DcScreenVO(), new UpsTelemetryStatusVO(), new DcScreenStatusVO());
            FanDetailDataVO detailData = InjectPropertiesUtil.injectByAnnotationCustomize(leftDataVO, number, backend, goldenUtil);

            return BaseResponse.success(detailData);
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
