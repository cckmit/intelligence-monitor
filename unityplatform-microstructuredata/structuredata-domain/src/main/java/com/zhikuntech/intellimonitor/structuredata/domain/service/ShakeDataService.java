package com.zhikuntech.intellimonitor.structuredata.domain.service;

import com.zhikuntech.intellimonitor.structuredata.domain.entity.ShakeData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tn
 * @since 2021-07-15
 */
public interface ShakeDataService extends IService<ShakeData> {
    /**
     * 动态获取风机结构检测数据
     * @param fanNumber
     * @return
     */
    ShakeData getFanData(String fanNumber);
}
