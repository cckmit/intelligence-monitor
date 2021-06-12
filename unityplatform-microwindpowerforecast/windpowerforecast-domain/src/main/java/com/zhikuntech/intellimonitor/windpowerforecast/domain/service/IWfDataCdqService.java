package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCdq;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 超短期功率预测 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
public interface IWfDataCdqService extends IService<WfDataCdq> {

    public void batchSave();
}
