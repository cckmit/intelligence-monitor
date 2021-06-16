package com.zhikuntech.intellimonitor.fanscada.domain.service;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanLeftDataVO;

import java.util.List;

/**
 * @author: DAI
 * @date: Created in 2021/6/15 17:38
 */
public interface FanDetailTwoService {

    /**
     * 查询数据
     *
     * @param fanId 风机Id
     * @return 结果
     */
    BaseResponse<FanLeftDataVO> getLeftData(Integer fanId);

}
