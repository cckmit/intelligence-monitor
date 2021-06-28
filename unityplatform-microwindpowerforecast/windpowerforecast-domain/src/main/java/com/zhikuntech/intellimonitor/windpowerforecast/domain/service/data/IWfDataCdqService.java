package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCdq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 超短期功率预测 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
public interface IWfDataCdqService extends IService<WfDataCdq> {

    /**
     * 批量保存数据
     */
    void batchSave();

    /**
     * 批量保存超短期数据
     * @param strings   数据行(待解析)
     */
    void batchProcessCdqData(List<String> strings);
}
