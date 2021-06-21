package com.zhikuntech.intellimonitor.fanscada.domain.service;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanModelDataVO;
import com.zhikuntech.intellimonitor.fanscada.domain.vo.FanModelDataVO;

/**
 * @ClassName: intelligence-monitor
 * @Description:Scada详情分图一服务类
 * @Author: 沈slk123
 * @CreateDate: 2021/6/16
 * @Version:
 */
public interface FanDetailOneService {
    /**
     * 查询数据
     *
     *
     * @param number 风机编号
     * @return 结果
     */
    BaseResponse<FanModelDataVO> getData(String number);
}
