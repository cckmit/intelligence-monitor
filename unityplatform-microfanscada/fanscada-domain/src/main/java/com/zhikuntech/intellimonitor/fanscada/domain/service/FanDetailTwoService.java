package com.zhikuntech.intellimonitor.fanscada.domain.service;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FsBasicParameterVO;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanDetailDataVO;


/**
 * @author: DAI
 * @date: Created in 2021/6/15 17:38
 */
public interface FanDetailTwoService {

    /**
     * 查询数据
     *
     * @param number 风机编号
     * @return 结果
     */
    BaseResponse<FanDetailDataVO> getData(String number);

    /**
     * 查询风机数据
     *
     * @param number 风机编号
     * @return 结果
     */
    BaseResponse<FsBasicParameterVO> getFanParameterByNumber(String number);

}
