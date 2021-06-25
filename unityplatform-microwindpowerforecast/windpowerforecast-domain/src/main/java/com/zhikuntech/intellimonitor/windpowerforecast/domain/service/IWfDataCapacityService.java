package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCapacity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 容量 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-18
 */
public interface IWfDataCapacityService extends IService<WfDataCapacity> {


    /**
     * 获取当月全场发电量
     * @param monthBg       yyyy-MM-dd HH:mm:ss
     * @param monthNextBg   yyyy-MM-dd HH:mm:ss
     * @return  全场发电量
     */
    BigDecimal fetchGenElectricWithDate(String monthBg, String monthNextBg);

}
