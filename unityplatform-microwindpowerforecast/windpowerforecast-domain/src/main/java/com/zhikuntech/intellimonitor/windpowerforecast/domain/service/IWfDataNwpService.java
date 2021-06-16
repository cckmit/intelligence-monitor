package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataNwp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 数值天气预报 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
public interface IWfDataNwpService extends IService<WfDataNwp> {

    List<WfDataNwp> queryBatch();

    List<BigDecimal> queryHigh();

    void batchSave();
}
