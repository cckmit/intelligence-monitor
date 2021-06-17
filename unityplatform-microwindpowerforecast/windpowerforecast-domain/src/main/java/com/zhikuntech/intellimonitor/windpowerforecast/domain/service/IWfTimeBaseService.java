package com.zhikuntech.intellimonitor.windpowerforecast.domain.service;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfTimeBase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 * 时间基准表 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-16
 */
public interface IWfTimeBaseService extends IService<WfTimeBase> {

    /**
     * 生成基准时间
     * <p>
     *     1 min
     *     5 min
     *     15 min
     * </p>
     */
    void generateCurDateData();

    /**
     * 生成基准时间
     * <p>
     *     1 min
     *     5 min
     *     15 min
     * </p>
     * @param localDate 日期
     */
    void generateCurDateData(LocalDate localDate);

    /**
     * 生成基准时间
     * <p>
     *     1 min
     *     5 min
     *     15 min
     * </p>
     * @param localDate 日期字符串 yyyy-MM-dd
     */
    void generateCurDateData(String localDate);
}
